package javasql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小省 on 2016/7/1.
 */
public class RDD2DataFrameDynamic {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("RDD2DataFrameDynamic")
				.setMaster("local[4]");

		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);

		//rdd --> df

		//rdd -->row
		JavaRDD<String> lines = sc.textFile("students.txt");
		JavaRDD<Row> row = lines.map(x -> RowFactory.create(Integer.valueOf(x.split(",")[0]), x.split(",")[1], Integer.valueOf(x.split(",")[2])));

		//fileds 顺序与fileds保持一致
		List<StructField> fileds = new ArrayList<>();
		fileds.add(DataTypes.createStructField("id", DataTypes.IntegerType, true));
		fileds.add(DataTypes.createStructField("name", DataTypes.StringType, true));
		fileds.add(DataTypes.createStructField("score", DataTypes.IntegerType, true));

		//schem
		StructType schem = DataTypes.createStructType(fileds);

		//df
		DataFrame df = sqlContext.createDataFrame(row, schem);
		df.show();

		//df-->rdd
		df.javaRDD().foreach(x -> System.out.println(x.getInt(0)));
		df.javaRDD().foreach(x -> System.out.println(x.schema()));
		sc.close();


	}
}
