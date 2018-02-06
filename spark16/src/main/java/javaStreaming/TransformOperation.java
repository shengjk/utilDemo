package javaStreaming;

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

/*
import com.google.common.base.Optional;
*/
public class TransformOperation {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("TransformOperation").setMaster("local[2]");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(20));
		
		// 用户对于网站上的广告可以进行点击！点击之后可以进行实时计算,但是有些用户就是刷广告！
		// 所以说我们要有一个黑名单机制！只要是黑名单中的用户点击的广告,我们就给过滤掉！
		
		// 先来模拟一个黑名单数据RDD,true代表启用,false代表不启用！
		List<Tuple2<String, Boolean>> blacklist = new ArrayList<Tuple2<String, Boolean>>();
		blacklist.add(new Tuple2<String, Boolean>("yasaka", true));
		blacklist.add(new Tuple2<String, Boolean>("xuruyun", false));
		
		final JavaPairRDD<String, Boolean> blacklistRDD = jssc.sc().parallelizePairs(blacklist);
		
		// time adId name
		JavaReceiverInputDStream<String> adsClickLogDStream = jssc.socketTextStream("spark001", 8888);
		
		JavaPairDStream<String, String> adsClickLogPairDStream = adsClickLogDStream.mapToPair(
		//第一个string是前台传过来的，第二、三个是返回过去的
		new PairFunction<String, String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split(" ")[2], line);
			}
		});
		
		JavaDStream<String> normalLogs = adsClickLogPairDStream.transform(new Function<JavaPairRDD<String,String>, JavaRDD<String>>() {

			private static final long serialVersionUID = 1L;

			@Override
			public JavaRDD<String> call(JavaPairRDD<String, String> userLogBatchRDD)
					throws Exception {
				
				JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> joinedRDD =
						userLogBatchRDD.leftOuterJoin(blacklistRDD);
				
				//过滤操作
				JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> filteredRDD = 
						joinedRDD.filter(new Function<Tuple2<String,Tuple2<String,Optional<Boolean>>>, Boolean>() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public Boolean call(Tuple2<String, Tuple2<String, Optional<Boolean>>> tuple)
							throws Exception {
						
						if(tuple._2._2.isPresent() && tuple._2._2.get()){
							return false;
						}
						
						return true;
					}
				});
				
				JavaRDD<String> validLogRDD = filteredRDD.map(new Function<Tuple2<String,Tuple2<String,Optional<Boolean>>>, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(
							Tuple2<String, Tuple2<String, Optional<Boolean>>> tuple)
							throws Exception {
						return tuple._2._1;
					}
				});
				
				return validLogRDD;
			}
		});
		
		normalLogs.print();
		
		jssc.start();
		jssc.awaitTermination();
		jssc.stop();
		jssc.close();
	}
}
