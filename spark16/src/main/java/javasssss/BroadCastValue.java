package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 小省 on 2016/7/11.
 */
/*
spark 广播变量，目的是让每一个RDD work都可以直接用，与java中的
static变量相似
 */
public class BroadCastValue {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("BroadCastValue").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);

		final int f =2;
		Broadcast<Integer>broadcast= sc.broadcast(f);
		List<Integer> list= Arrays.asList(1,2,3,4,5,6);

		JavaRDD<Integer> lisRDD=sc.parallelize(list);
		lisRDD.foreach(x -> {

			Integer.valueOf(x*broadcast.value());
			System.out.println(x);
		});


		sc.close();

	}
	
	}
