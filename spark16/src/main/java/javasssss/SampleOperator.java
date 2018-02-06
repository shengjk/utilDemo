package javasssss;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;


//�����������ȡ����
public class SampleOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SampleOperator")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<String> names = Arrays
				.asList("xurunyun", "liangyongqi", "wangfei","yasaka");
		JavaRDD<String> nameRDD = sc.parallelize(names,2);
		
		nameRDD.sample(false, 0.75, 1L).foreach(new VoidFunction<String>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void call(String name) throws Exception {
				System.out.println(name);
			}
		});;
	}
}
