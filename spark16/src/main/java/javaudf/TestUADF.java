package javaudf;

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

import java.util.Arrays;
import java.util.List;

/**
 * Created by shengjk1 on 2016/8/9.
 */
public class TestUADF {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("TestUDF")
				.setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		SQLContext sqlContext=new SQLContext(sc);
		
		List<String> list= Arrays.asList("Yasaka", "Xuruyun", "Wangfei", "Liangyongqi", "Xuruyun", "Xuruyun");
		JavaRDD nameRDD= sc.parallelize(list);
		//java版 RDD =>DF
		//1.RowRDD
		JavaRDD<Row> nameRowRDD=nameRDD.map(name -> RowFactory.create(name));
		//2.元数据
		List<StructField> stf =Arrays.asList(DataTypes.createStructField("name", DataTypes.StringType,true));
		StructType st=DataTypes.createStructType(stf);
		//3.生成df
		DataFrame df =sqlContext.createDataFrame(nameRowRDD,st);
		
		df.registerTempTable("names");
		sqlContext.udf().register("strGroupCount", new StringGroupCount());
		Row[] re=sqlContext.sql("select name, strGroupCount(name) from names group by name")
				.collect();
		Arrays.asList(re).forEach(x-> System.out.println(x));
		
	}
		
		
}
