package javaStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by shengjk on 2016/8/9.
 */
public class StreamingWC {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("StreamingWC")
				.setMaster("local[2]");
		JavaStreamingContext ssc =new JavaStreamingContext(conf, Durations.seconds(10));
		
		JavaReceiverInputDStream<String> lines= ssc.socketTextStream("localhost",8888);
		JavaDStream<String> line =lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" "));
			}
		});
		JavaPairDStream<String,Integer> pairline= line.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s,1);
			}
		});
		
		JavaPairDStream<String,Integer> result=pairline.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		});
		
		result.print();
		
		ssc.start();
		ssc.awaitTermination();
		ssc.close();
	}
}
