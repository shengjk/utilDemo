package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.List;

public class FilterOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("LineCount").setMaster(
				"local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// filter算子是过滤，里面的逻辑如果返回的是true就保留下来，false就过滤掉
		/*
		FILTER的具体内部实现是一个迭代器，然后一个一个的进行操作
		 */
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
		JavaRDD<Integer> numberRDD = sc.parallelize(numbers);
		
		//每一条记录都会调用filter算子内部类
		JavaRDD<Integer> results = numberRDD.filter(new Function<Integer, Boolean>() {

					private static final long serialVersionUID = 1L;
					@Override
					public Boolean call(Integer number) throws Exception {
						return number % 2 == 0;
					}
				});
		
		results.foreach(new VoidFunction<Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void call(Integer result) throws Exception {
				System.out.println(result);
			}
		});

		sc.close();
	}
}
