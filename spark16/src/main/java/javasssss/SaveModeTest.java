package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;

public class SaveModeTest {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SaveModeTest").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		
		DataFrame peopleDF = sqlContext.read().format("json").load("people.json");
//		peopleDF.save("people2.json", SaveMode.ErrorIfExists);
//		peopleDF.save("people2.json", SaveMode.Append);
//		peopleDF.save("people2.json", SaveMode.Ignore);
		peopleDF.save("people2.json", SaveMode.Overwrite);
	}
}
