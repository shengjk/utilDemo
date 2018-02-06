package javaStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

public class WindowBasedTopWOrd {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("WindowBasedTopWOrd").setMaster("local[2]")
				.set("spark.default.parallelism", "100");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		
		// 这里叫log日志, yasaka hello , xuruyun world
		JavaReceiverInputDStream<String> log = jssc.socketTextStream("spark001", 8888);
		JavaDStream<String> searchWord = log.map(new Function<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String call(String searchLog) throws Exception {
				return searchLog.split(" ")[1];
			}
		});
		
		JavaPairDStream<String, Integer> pairs = searchWord.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String, Integer>(word ,1);
			}
		});
		
		JavaPairDStream<String, Integer> wordcounts = pairs.reduceByKeyAndWindow(
				new Function2<Integer, Integer, Integer>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		},Durations.seconds(60), Durations.seconds(10));
		
		wordcounts.print();
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
		
	}
}
