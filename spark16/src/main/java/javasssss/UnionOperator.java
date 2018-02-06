package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

//����ȥ�أ�����RDD���1��RDD
public class UnionOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SampleOperator")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<String> names = Arrays
				.asList("xurunyun", "liangyongqi", "wangfei","yasaka");
		List<String> names1 = Arrays
				.asList("xurunyun", "liangyongqi", "wangfei3","yasaka4");
		JavaRDD<String> nameRDD = sc.parallelize(names,2);
		JavaRDD<String> nameRDD1 = sc.parallelize(names1,2);
		
		nameRDD.union(nameRDD1).foreach(x-> System.out.println(x));;
		
		sc.close();
	}
}
