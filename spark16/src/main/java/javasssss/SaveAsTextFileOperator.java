package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 小省  on 2016/6/16.
 */
public class SaveAsTextFileOperator {
	public static void main(String[] args) {
//		SparkConf conf =new SparkConf().setAppName("SaveAsTextFileOperator").setMaster("local");
//		JavaSparkContext sc =new JavaSparkContext(conf);
//
//		List<String> list= Arrays.asList("tasaka","yasaka1","yasaka2");
//		JavaRDD<String> litRDD= sc.parallelize(list);
////		litRDD.saveAsTextFile("hdfs://10.16.30.54:8020/oo");
//		JavaRDD<String>  name=litRDD.map(new Function<String, String>() {
//			public String call(String s) throws Exception {
//				return s+"sss";
//			}
//		});
//		name.saveAsTextFile("C:/as");
		SparkConf conf = new SparkConf().setAppName("SaveAsTextFileOperator")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// 有一个集合，里面有1到10，10个数字，现在我们通过reduce来进行累加
		List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5);
		JavaRDD<Integer> numbers = sc.parallelize(numberList,1);

		JavaRDD<Integer> doubledNumbers = numbers.map(new Function<Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v) throws Exception {
				return v * 2;
			}
		});

		doubledNumbers.saveAsTextFile("hdfs://10.16.30.54:8020/save.txt");
		sc.close();
	}
}
