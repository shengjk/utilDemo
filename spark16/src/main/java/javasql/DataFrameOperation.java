package javasql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class DataFrameOperation {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("DataFrameOperation").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		
		// 把数据框读过来完全可以理解为一张表！
		DataFrame df = sqlContext.read().json("students.json");
		
		// 打印这张表
		df.show();
		
		// 打印元数据
		df.printSchema();
		
		// 查询列并计算
		df.select("name").show();
		df.select(df.col("name"),df.col("score").plus(1)).show();
		
		// 过滤
		df.filter(df.col("score").gt(80)).show();
		
		// 根据某一列分组然后统计count
		df.groupBy("score").count().show();
		
		sc.close();
	}
}
