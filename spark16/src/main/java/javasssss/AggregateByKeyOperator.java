package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class AggregateByKeyOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf()
			.setAppName("AggregateByKeyOperator")
			.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		
		JavaRDD<String> lines = sc.textFile("CHANGES.txt");
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(" "));
			}
		});
		JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word ,1);
			}
		});
		
		// aggregateByKey其实和这个reduceByKey差不多，reduceByKey是aggregateByKey简化版
		// aggregateByKey里面的参数需要三个
		// 第一个参数，每个Key的初始值
		// 第二个参数，Seq Function，如何进行Shuffle map-side的本地聚合
		// 第三个参数，说白了就是如何进行Shuffle reduce-side的全局聚合
		
		// reduce foldLeft
		
		JavaPairRDD<String, Integer> wordCounts = pairs.aggregateByKey(10
				, new Function2<Integer,Integer,Integer>(){

					private static final long serialVersionUID = 1L;

					@Override
					public Integer call(Integer v1, Integer v2)
							throws Exception {
						return v1+v2;
					}
			
		},  new Function2<Integer,Integer,Integer>(){

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2)
					throws Exception {
				return v1+v2;
			}
	
		});
		
		List<Tuple2<String,Integer>> list = wordCounts.collect();
		for(Tuple2<String,Integer> wc : list){
			System.out.println(wc);
		}
		
		sc.close();
	}
}
