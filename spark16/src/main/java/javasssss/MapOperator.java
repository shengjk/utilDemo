package javasssss;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

public class MapOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("LineCount")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        
        List<Integer> numbers = Arrays.asList(1,2,3,4,5);
        JavaRDD<Integer> numberRDD = sc.parallelize(numbers);
        // map对每个元素进行操作
        JavaRDD<Integer> results = numberRDD.map(new Function<Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer number) throws Exception {
				return number * 10;
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
