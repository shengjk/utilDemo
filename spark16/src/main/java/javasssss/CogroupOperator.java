package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 小省  on 2016/6/14.
 */
/*
cogroup不同于join，1.结果类型不同。2.不完全匹配

student id : 4
student name: []
student score: [40]
student id : 5
student name: [yasaka1]
student score: []
student id : 2
student name: [wangfei]
student score: [90, 60]
student id : 3
student name: [yasaka]
student score: [80, 50]
student id : 1
student name: [xuruyun]
student score: [100, 70]

 */
public class CogroupOperator {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("CogroupOperator").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);
		List<Tuple2<String,String>> studentList= Arrays.asList(
			new Tuple2<String, String>("1","xuruyun"),
			new Tuple2<String, String>("2","wangfei"),
			new Tuple2<String, String>("3","yasaka"),
			new Tuple2<String, String>("5","yasaka1")
		);
		List<Tuple2<String,String>> scoreList=Arrays.asList(
				new Tuple2<String,String>("1","100"),
				new Tuple2<String,String>("2","90"),
				new Tuple2<String,String>("3","80"),
				new Tuple2<String,String>("1","70"),
				new Tuple2<String,String>("2","60"),
				new Tuple2<String,String>("3","50")
		,new Tuple2<String,String>("4","40"));

		JavaPairRDD<String,String> students = sc.parallelizePairs(studentList);
		JavaPairRDD<String,String> scores=sc.parallelizePairs(scoreList);
		JavaPairRDD<String, Tuple2<Iterable<String>, Iterable<String>>> stuscore= students.cogroup(scores);

		stuscore.foreach(new VoidFunction<Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>>>() {
			@Override
			public void call(Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>> stringTuple2Tuple2) throws Exception {

				System.out.println("student id : "+stringTuple2Tuple2._1);
				System.out.println("student name: "+stringTuple2Tuple2._2._1);
				System.out.println("student score: "+stringTuple2Tuple2._2._2);
			}
		});
		sc.close();



	}
}
