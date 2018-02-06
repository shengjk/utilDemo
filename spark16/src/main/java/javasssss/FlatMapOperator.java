package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.List;

public class FlatMapOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("LineCount").setMaster(
				"local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<String> lineList = Arrays.asList("hello xuruyun" , "hello xuruyun", "hello wangfei");
		JavaRDD<String> lines = sc.parallelize(lineList);
		
		// flatMap = flat + map  底层也是一个Iterator，以mappartition为单位的，内部类是作为一个函数传来传去的
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(" "));
			}
		});
		
		words.foreach(new VoidFunction<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(String result) throws Exception {
				System.out.println(result);
			}
		});
		
		sc.close();
	}
}
