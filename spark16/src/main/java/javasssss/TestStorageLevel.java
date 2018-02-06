package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;

public class TestStorageLevel {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("TestStorageLevel")
				.setMaster("local");
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		// 先是没有做缓存的版本
		// 1569898 cost 3390 milliseconds.
		// 1569898 cost 725 milliseconds.
		
		// 下面我们来一个做了缓存的版本
		// 1569898 cost 4436 milliseconds.
		// 1569898 cost 102 milliseconds.
		
		// RDD持久化是spark里面非常非常重要的一个功能cache persist
		// 默认的缓存策略就是MEMORY_ONLY
		
		// 如何对RDD的持久化缓存策略进行选择？？？
		// 1,Cache() MEMORY_ONLY
		// 2,MEMORY_ONLY_SER
		// 3,如果缓存需要一些高可靠性,选择 _2
		// 4,能使用内存就不使用磁盘
		
		JavaRDD<String> lines = sc.textFile("NASA_access_log_Aug95");
		lines = lines.persist(StorageLevel.MEMORY_ONLY_SER());
		
		long beginTime = System.currentTimeMillis();
		long count = lines.count();
		System.out.println(count);
		long endTime = System.currentTimeMillis();
		System.out.println("cost " + (endTime-beginTime) + " milliseconds.");
		
		beginTime = System.currentTimeMillis();
		count = lines.count();
		System.out.println(count);
		endTime = System.currentTimeMillis();
		System.out.println("cost " + (endTime-beginTime) + " milliseconds.");
		
		sc.close();
	}
}
