package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

//sample(���ȡ��)֮��take(��˳��)ȡ��λ
public class TakeSample {

	// takeSample = take + sample
	
	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir", "E:\\work\\software\\hadoop-2.6.0\\bin");
		SparkConf conf = new SparkConf().setAppName("SampleOperator")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<String> names = Arrays
				.asList("xuruyun", "liangyongqi", "wangfei","xuruyun");
		JavaRDD<String> nameRDD = sc.parallelize(names,1);
		//true���ܻ����ظ�ȡ��
		List<String> list = nameRDD.takeSample(false,2);
		for(String name :list){
			System.out.println(name);
		}
	}
}
