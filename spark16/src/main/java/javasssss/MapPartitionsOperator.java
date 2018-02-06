package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小省 on 2016/7/8.
 */

/*
以一个partition作为操作的最小单元，注意oom
 */
public class MapPartitionsOperator {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("MapPartitonsWithIndexOperator").setMaster(
				"local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> names = Arrays.asList("xurunyun","liangyongqi","wangfei");
		JavaRDD<String> nameRDD = sc.parallelize(names);

		Map<String,Integer> scoreM=new HashMap<String, Integer>();
		scoreM.put("xurunyun", 150);
		scoreM.put("liangyongqi", 100);
		scoreM.put("wangfei", 90);
		


		sc.close();
	}




}
