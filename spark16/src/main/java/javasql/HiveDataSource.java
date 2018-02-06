package javasql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**javasql.HiveDataSource
 * Created by 小省 on 2016/7/7.
 */

/*
If Hive dependencies can be found on the classpath,
Spark will load them automatically.
Note that these Hive dependencies must also be present on all of the worker nodes,
as they will need access to the Hive serialization and deserialization libraries (SerDes) in order to access data stored in Hive.

 */
public class HiveDataSource {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("HiveDataSource");
		JavaSparkContext sc =new JavaSparkContext(conf);
		
		//默认的都在操作linux文件系统  hdfs://
		sc.textFile("/tmp/test").saveAsTextFile("/aaa");
		

//		HiveContext hiveContext=new HiveContext(sc.sc());
//
////		hiveContext.sql("drop table if exists testtest ");
////		hiveContext.sql("create table testtest1(id string,name string)")
// DataFrame df=hiveContext.sql("select * from test");
//		df.show();
//		//默认都存在了本地
//
//		df.write().mode(SaveMode.Overwrite).save("/hivetest");
//		df.write().mode(SaveMode.Overwrite).saveAsTable("goodTest");
//		df.write().parquet("/hivetestParquet");
		sc.close();
	}
}
