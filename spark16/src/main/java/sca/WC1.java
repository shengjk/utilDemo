package sca;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by 小省 on 2016/6/29.
 */
public class WC1 {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("WC1").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> lines=sc.textFile("spark.iml");
		lines.flatMap(x-> Arrays.asList(x.split(" ")))
				.mapToPair(x -> new Tuple2<String, Integer>(x,1))
				.reduceByKey((x,y)->x+y)
				.foreach(x -> System.out.println(x));

		
		//java7
		lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String s) throws Exception {
				
				return Arrays.asList(s.split(","));
			}
		})
		.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s,1);
			}
		})
		.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		})
		.foreach(new VoidFunction<Tuple2<String, Integer>>() {
			@Override
			public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
				System.out.println(stringIntegerTuple2._1+"======"+stringIntegerTuple2._2);
			}
		});

//		.flatMap(x ->Arrays.asList(x.split(" ")))
//				.mapToPair(x ->new Tuple2<String, Integer>(x,1))
//				.reduceByKey((x,y) -> x+y)
//				.foreach(x -> System.out.println(x));
//		filter(s -> s.contains("error"));


	}
}
