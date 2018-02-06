package javaudf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jk on 2016/8/9.
 */
public class TestUDF {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("TestUDF")
				.setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		SQLContext sqlContext=new SQLContext(sc);
		
		List<String> nameList = Arrays.asList("Yasaka", "Xuruyun", "Wangfei", "Liangyongqi");
		JavaRDD<String> nameRDD=sc.parallelize(nameList);
		//RDD =>DF
		//1.Row
		JavaRDD<Row> nameROwRDD=nameRDD.map(new Function<String, Row>() {
			private static final long serialVersionUID = -4127557630793841496L;
			
			@Override
			public Row call(String v1) throws Exception {
				
				return RowFactory.create(v1);
			}
		});
		//2.元数据
		List<StructField> fle=new ArrayList<>();
		fle.add(DataTypes.createStructField("name",DataTypes.StringType,true));
		StructType schema = DataTypes.createStructType(fle);
		//创建df
		DataFrame df=sqlContext.createDataFrame(nameROwRDD,schema);
		df.registerTempTable("names");
		
		sqlContext.udf().register("strLen", new UDF1<String, Integer>() {
			private static final long serialVersionUID = -7528118184195297508L;
			
			@Override
			public Integer call(String s) throws Exception {
				return s.length();
			}
		}, DataTypes.IntegerType);
		
		sqlContext.sql("select name,strlen(name) from names").show();
		
		
	}
}
