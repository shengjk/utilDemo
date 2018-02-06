package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 小省  on 2016/6/15.
 */
public class CartesianOperator {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("CartesianOperator").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);

		List<String> clothes = Arrays.asList("T恤衫","夹克","皮大衣","衬衫","毛衣");
		List<String> trousers = Arrays.asList("西裤","内裤","铅笔裤","皮裤","牛仔裤");
		JavaRDD<String> clothesRDD = sc.parallelize(clothes);
		JavaRDD<String> trousersRDD = sc.parallelize(trousers);

		
		trousersRDD.count();
		JavaPairRDD<String,String> result= clothesRDD.cartesian(trousersRDD);

		for (Tuple2<String,String> as:result.collect()){
			System.out.println(as);
		}

		sc.close();



	}
}
