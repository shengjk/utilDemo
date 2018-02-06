package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.List;

public class DinstinctOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SampleOperator")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<String> names = Arrays
				.asList("xuruyun", "liangyongqi", "wangfei","xuruyun","wangfei","liangyongqi");
		JavaRDD<String> nameRDD = sc.parallelize(names,2);
		
		nameRDD.distinct().foreach(new VoidFunction<String>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void call(String name) throws Exception {
				System.out.println(name);
			}
		});
	}
}
