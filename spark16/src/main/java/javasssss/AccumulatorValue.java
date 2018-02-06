package javasssss;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 小省 on 2016/7/11.
 */
/*
 spark 累加器 accumulator
 */

public class AccumulatorValue {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("AccumulatorValue").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);

		final  Accumulator<Integer> sum=sc.accumulator(0,"Our Accumulator");

		List<Integer> list= Arrays.asList(1,2,3,4,5,6);

		JavaRDD<Integer> listRdd=sc.parallelize(list);

		listRdd.foreach(new VoidFunction<Integer>() {
			@Override
			public void call(Integer integer) throws Exception {
				//计数器去操作其他的数
				sum.add(integer);
				//可以直接进行打印，但通过sum.value方法就会报错  Can't read accumulator value in task
				System.out.println(sum);

			}
		});

		//凡是不在RDD中的代码都属于客户端driver
		System.out.println(sum.value());
		sc.close();
	}
}
