package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

/**
 * Created by 小省 on 2016/6/21.
 */
public class TopN {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("TopN").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);

		JavaRDD<String> lines= sc.textFile("top.txt");
		JavaPairRDD<Integer,String> pairs=lines.mapToPair(x ->new Tuple2<Integer, String>(Integer.valueOf(x),x));

		JavaRDD<String> result =pairs.sortByKey(false).map(x -> x._2);
		for(String re:result.take(10)){
			System.out.println(re);
		}

	}
}
