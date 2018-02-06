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
join等同于sql中的join 完全匹配  是按照key进行join的。
rdd1 join rdd2  join返回结果key tuple(value1,value2),注意value与rdd join顺序的关系
key : 2
student name: wangfei
student score 90
key : 2
student name: wangfei
student score 60
key : 3
student name: yasaka
student score 80
key : 3
student name: yasaka
student score 50
key : 1
student name: xuruyun
student score 100
key : 1
student name: xuruyun
student score 70
 */
public class JoinOperator {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("CogroupOperator").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);
		List<Tuple2<String,String>> studentList= Arrays.asList(
				new Tuple2<String, String>("1","xuruyun"),
				new Tuple2<String, String>("2","wangfei"),
				new Tuple2<String, String>("3","yasaka"),
				new Tuple2<String, String>("4","yasaka1")
		);
		List<Tuple2<String,String>> scoreList=Arrays.asList(
				new Tuple2<String,String>("1","100"),
				new Tuple2<String,String>("2","90"),
				new Tuple2<String,String>("3","80"),
				new Tuple2<String,String>("1","70"),
				new Tuple2<String,String>("2","60"),
				new Tuple2<String,String>("3","50"),
				new Tuple2<String,String>("5","550")
		);

		JavaPairRDD<String,String> students = sc.parallelizePairs(studentList);
		JavaPairRDD<String,String> scores=sc.parallelizePairs(scoreList);

		JavaPairRDD<String, Tuple2<String,String>> stuscore=students.join(scores);
		stuscore.foreach(new VoidFunction<Tuple2<String, Tuple2<String, String>>>() {
			@Override
			public void call(Tuple2<String, Tuple2<String, String>> stringTuple2Tuple2) throws Exception {
				System.out.println("key : "+stringTuple2Tuple2._1);
				System.out.println("student name: "+stringTuple2Tuple2._2._1);
				System.out.println("student score "+stringTuple2Tuple2._2._2);
			}
		});
		sc.close();
	}
}
