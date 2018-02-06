package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 小省 on 2016/7/7.
 */
public class MapPartitionsWithIndexOperator {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("MapPartitonsWithIndexOperator").setMaster(
				"local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		List<String> names = Arrays
				.asList("xurunyun", "liangyongqi", "wangfei");

		JavaRDD<String> lines= sc.parallelize(names);

//		lines.mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {
//			@Override
//			public Iterator<String> call(Integer v1, Iterator<String> v2) throws Exception {
//				return null;
//			}
//		});

	}
}
