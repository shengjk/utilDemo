package javasssss.df;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class DataFrameCreate {

	public static void main(String[] args) {
		
		/*
		 *sparksession替换hivecontext,sqlcontext
		 */
		
		SparkConf conf = new SparkConf().setAppName("DataFrameCreate").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		HiveContext sqlContext = new HiveContext(sc);
		
		DataFrame df = sqlContext.read().json("students.json");
		/*
		+--------------------+-----+
|                name|score|
+--------------------+-----+
|{"name":"xuruyun"...|   99|
|             xuruyun|   99|
|             xuruyun|   99|
|         liangyongqi|   74|
+--------------------+-----+
		 */
		df.show();
		df.registerTempTable("studentTable");
		System.out.println(df.schema().mkString());
		System.out.println(df.columns().toString());
		
		
		
		
		List<Tuple2<String,Long>> list=new ArrayList<>();
		list.add(new Tuple2<>("xuruyun",12L));
		list.add(new Tuple2<>("xuruyun1",121L));
		list.add(new Tuple2<>("xuruyun2",122L));
		JavaPairRDD<String,Long> javaPairRDD=sc.parallelizePairs(list);
		
		JavaRDD<Row> javaRDDRow=javaPairRDD.map(new Function<Tuple2<String,Long>, Row>() {
			@Override
			public Row call(Tuple2<String, Long> v1) throws Exception {
				return RowFactory.create(v1._1(),v1._2());//这些都是value
			}
		});
		
		List<StructField> fields=new ArrayList<>();//schema才是字段名
		fields.add(DataTypes.createStructField("name", DataTypes.StringType,true));
		fields.add(DataTypes.createStructField("ipLength", DataTypes.LongType,true));
		
		StructType schema= DataTypes.createStructType(fields);
		DataFrame IpDf=sqlContext.createDataFrame(javaRDDRow,schema);
		IpDf.show();
		IpDf.registerTempTable("ipTable");
		
		sqlContext.sql("select distinct * from studentTable as a left join ipTable as b on a.name=b.name").show();
		
		/*
		
		+--------------------+-----+-------+--------+
|                name|score|   name|ipLength|
+--------------------+-----+-------+--------+
|{"name":"xuruyun"...|   99|   null|    null|
|             xuruyun|   99|xuruyun|      12|
|         liangyongqi|   74|   null|    null|
+--------------------+-----+-------+--------+
		
		
		
		+--------------------+-----+-------+--------+
|                name|score|   name|ipLength|
+--------------------+-----+-------+--------+
|{"name":"xuruyun"...|   99|   null|    null|
|             xuruyun|   99|xuruyun|      12|
|             xuruyun|   99|xuruyun|      12|
|         liangyongqi|   74|   null|    null|
+--------------------+-----+-------+--------+
	
		
		 */
		
		
//
//		List<String> list1=new ArrayList<>();
//		list1.add("name");
//		list1.add("name");
//		df.join(IpDf, (Column) list1, "left_outer").show();
		
		
		
		sc.close();
	}
}
