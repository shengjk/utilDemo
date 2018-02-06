package streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class HDFSWordcount {

	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("HDFSWordcount")
				.setMaster("local");
		JavaStreamingContext ssc =new JavaStreamingContext(conf, Durations.seconds(10));
		
		JavaDStream<String>lines= ssc.textFileStream("hdfs://10.16.30.54:8020/test/");
		JavaDStream<String> line=lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" "));
			}
		});
		JavaPairDStream<String,Integer>linePair= line.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 3831624405974032297L;
			
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s,1);
			}
		});
		
		JavaPairDStream<String,Integer> resu=linePair.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = -1047346698615454767L;
			
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		});
		resu.print();
		
		ssc.start();
		ssc.awaitTermination();
		ssc.stop();
		
	}
}
