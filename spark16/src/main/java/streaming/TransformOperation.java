package streaming;

import com.google.common.base.Optional;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengjk1 on 2016/8/15.
 */
public class TransformOperation {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("TransformOperation").
				setMaster("local[2]");
		JavaStreamingContext ssc =new JavaStreamingContext(conf, Durations.seconds(5));
		
		List<Tuple2<String, Boolean>> blacklist = new ArrayList<Tuple2<String, Boolean>>();
		blacklist.add(new Tuple2<String, Boolean>("yasaka", true));
		blacklist.add(new Tuple2<String, Boolean>("xuruyun", false));
		
		final JavaPairRDD<String, Boolean> blacklistRDD = ssc.sc().parallelizePairs(blacklist);
		
		JavaReceiverInputDStream<String> lines= ssc.socketTextStream("centos4",8888);
		JavaPairDStream<String,String> adsClickLogPairDStream= lines.mapToPair(new PairFunction<String, String, String>() {
			@Override
			public Tuple2<String, String> call(String s) throws Exception {
				return new Tuple2<String, String>(s.split(" ")[2],s);
			}
		});
		
		JavaDStream<String> normalLogs=adsClickLogPairDStream.transform(new Function<JavaPairRDD<String, String>, JavaRDD<String>>() {
			@Override
			public JavaRDD<String> call(JavaPairRDD<String, String> v1) throws Exception {
				JavaPairRDD<String,Tuple2<String,Optional<Boolean>>>joinedRDD= v1.leftOuterJoin(blacklistRDD);
				
				  JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> filteredRDD=joinedRDD.filter(new Function<Tuple2<String, Tuple2<String, Optional<Boolean>>>, Boolean>() {
					@Override
					public Boolean call(Tuple2<String, Tuple2<String, Optional<Boolean>>> v1) throws Exception {
						if (v1._2._2.isPresent() && v1._2._2.get()){
							return false;
						}
						return true;
					}
				});
				
				JavaRDD<String>validLogRDD=filteredRDD.map(new Function<Tuple2<String,Tuple2<String,Optional<Boolean>>>, String>() {
					@Override
					public String call(Tuple2<String, Tuple2<String, Optional<Boolean>>> v1) throws Exception {
						return v1._2._1;
					}
				});
				
				return validLogRDD;
			}
		});
		normalLogs.print();
		
		ssc.start();
		ssc.awaitTermination();
		ssc.stop();
		ssc.close();
		
	}
}
