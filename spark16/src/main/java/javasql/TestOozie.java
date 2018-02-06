package javasql;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengjk1 on 2017/1/5.
 */
public class TestOozie {
	private static Logger logger = Logger.getLogger(TestOozie.class);
	public static void main(String[] args) {
		SparkConf conf=new SparkConf().setAppName("TestOozie");
		JavaSparkContext javaSparkContext=new JavaSparkContext(conf);
		HiveContext sqlContext=new HiveContext(javaSparkContext);
		
		
		Map<String, String> options = new HashMap<String, String>();
		options.put("url", "jdbc.url");
		options.put("dbtable", "test.china_ip_district_v2_update");
		options.put("driver", "com.mysql.jdbc.Driver");
		options.put("user", "jdbc.user");
		options.put("password", "jdbc.password");
		//DataFrame一个partition,并且整个表全部加载到内存中
		DataFrame jdbc = sqlContext.read().options(options).format("jdbc").load();
		logger.info("jdbc.javaRDD().getNumPartitions()======================= "+jdbc.javaRDD().getNumPartitions());
		jdbc.show();
		jdbc.registerTempTable("jdbc");
		sqlContext.sql("select count(*) from jdbc").show();
		
		
		//Partitioning incompletely specified
		options.put("fetchSize","10000");
		options.put("partitionColumn","id");
		options.put("lowerBound","3");
		options.put("upperBound","2891427");
		options.put("numPartitions","3");
		//DataFrame依旧是一个partition
		DataFrame jdbc1 = sqlContext.read().options(options).format("jdbc").load();
		logger.info("jdbc.javaRDD()1.getNumPartitions()======================= "+jdbc.javaRDD().getNumPartitions());
		jdbc.show();
		jdbc.registerTempTable("jdbc1");
		sqlContext.sql("select count(*) from jdbc1").show();
		
		/*
		val jdbcDF = spark.read.format("jdbc").options(
     |   Map("url" ->  "jdbc:mysql://localhost:3306/ontime?user=root&password=mysql",
     |   "dbtable" -> "ontime.ontime_sm",
     |   "fetchSize" -> "10000",
     |   "partitionColumn" -> "yeard", "lowerBound" -> "1988", "upperBound" -> "2015", "numPartitions" -> "48"
     |   )).load()
		 */
				
		
	}
}
