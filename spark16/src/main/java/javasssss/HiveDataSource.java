package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;


/*
��Ҫ��hive-site.xml������spark  conf   --file

*/
public class HiveDataSource {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("HiveDataSource");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		HiveContext hiveContext = new HiveContext(sc.sc());
		
		hiveContext.sql("DROP TABLE IF EXISTS student_infos");
		hiveContext.sql("CREATE TABLE IF NOT EXISTS student_infos ( name STRING , age INT )");
		hiveContext.sql("LOAD DATA LOCAL INPATH '/usr/hadoopsoft/spark-1.6.0-bin-hadoop2.4/student_infos.txt' INTO TABLE student_infos");
	
		hiveContext.sql("DROP TABLE IF EXISTS student_scores");
		hiveContext.sql("CREATE TABLE IF NOT EXISTS student_scores ( name STRING , score INT )");
		hiveContext.sql("LOAD DATA LOCAL INPATH '/usr/hadoopsoft/spark-1.6.0-bin-hadoop2.4/student_scores.txt' INTO TABLE student_scores");
	
		DataFrame goodStudentsDF = hiveContext.sql("SELECT si.name, si.age, ss.score FROM student_infos si JOIN student_scores ss ON si.name=ss.name WHERE ss.score>=80");
	
		hiveContext.sql("DROP TABLE IF EXISTS good_student_infos");
		goodStudentsDF.saveAsTable("good_student_infos");
		
		
	}
}
