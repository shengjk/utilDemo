package javasssss.df;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class DataFrameCreate1 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("DataFrameCreate").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		
		DataFrame df = sqlContext.read().json("students.json");
		df.show();
		System.out.println(df.schema().mkString());
		System.out.println(df.columns().toString());
		
		List<Tuple2<String,Long>> list=new ArrayList<>();
		list.add(new Tuple2<>("xuruyun",12L));
		list.add(new Tuple2<>("xuruyun1",121L));
		list.add(new Tuple2<>("xuruyun2",122L));
		JavaPairRDD<String,Long> javaPairRDD=sc.parallelizePairs(list);
		 
		
		
		sc.close();
	}
}
