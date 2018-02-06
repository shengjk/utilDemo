package javasql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;


/**
 * Created by 小省 on 2016/7/1.
 */
public class LoadSave {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("LoadSave").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		SQLContext sqlContext =new SQLContext(sc);
//		DataFrame df= sqlContext.read().parquet("users.parquet");
////				.load("users.parquet");
//		df.show();
//		df.write().mode(SaveMode.Overwrite).save("nan");
//		df.save("na", SaveMode.Overwrite);

		sqlContext.sql("select * from text.students.txt").show();
		
		sc.stop();


	}
}
