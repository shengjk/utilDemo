package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCDataSource {

	/*
	 * ./bin/spark-submit --master spark://spark001:7077 --class com.shsxt.study.sql.JDBCDataSource 
	 * --driver-class-path ./lib/mysql-connector-java-5.1.32-bin.jar 
	 * --jars ./lib/mysql-connector-java-5.1.32-bin.jar  
	 * sparksqljdbcdatasource.jar 
	 
	 export SPARK_CLASSPATH=$SPARK_CLASSPATH:/usr/hadoopsoft/spark-1.6.0-bin-hadoop2.4/lib/mysql-connector-java-5.1.32-bin.jar
	 
	 */
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("JDBCDataSource");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		
		Map<String,String> options = new HashMap<String,String>();
		options.put("url", "jdbc:mysql://spark001:3306/test");
		options.put("dbtable", "student_infos");
		options.put("user","root");
		options.put("password", "123123");
		DataFrame studentInfosDF = sqlContext.read().format("jdbc").options(options).load();
		
		options.put("dbtable", "student_scores");
		DataFrame studentScoresDF = sqlContext.read().format("jdbc").options(options).load();
		
		// 我们将两个DataFrame转换成JavaPairRDD，进行join操作
		JavaPairRDD<String, Tuple2<Integer, Integer>> studentsRDD = studentInfosDF.javaRDD().mapToPair(new PairFunction<Row, String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(Row row) throws Exception {
				return new Tuple2<String,Integer>(row.getString(0)
						, Integer.valueOf(String.valueOf(row.get(1))));
			}
		}).join(studentScoresDF.javaRDD().mapToPair(new PairFunction<Row, String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(Row row) throws Exception {
				return new Tuple2<String,Integer>(row.getString(0)
						, Integer.valueOf(String.valueOf(row.get(1))));
			}
		}));
		
		// 过滤
		JavaRDD<Row> studentsRowRDD = studentsRDD.map(new Function<Tuple2<String,Tuple2<Integer,Integer>>, Row>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Row call(Tuple2<String, Tuple2<Integer, Integer>> tuple)
					throws Exception {
				return RowFactory.create(tuple._1, tuple._2._1, tuple._2._2);
			}
		}).filter(new Function<Row, Boolean>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean call(Row row) throws Exception {
				if(row.getInt(2)>80){
					return true;
				}
				return false;
			}
		});
		
		List<StructField> structFileds = new ArrayList<StructField>();
		structFileds.add(DataTypes.createStructField("name", DataTypes.StringType, true));
		structFileds.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));
		structFileds.add(DataTypes.createStructField("score", DataTypes.IntegerType, true));
		StructType schema = DataTypes.createStructType(structFileds);
		
		DataFrame studentsDF = sqlContext.createDataFrame(studentsRowRDD, schema);
		Row[] rows = studentsDF.collect();
		for(Row row : rows){
			System.out.println(row);
		}
		
		// 将DataFrame数据保存到MySQL表中
		// 这种方式在公司里面会很常用的！有可能插入MySQL，也有可能插入Redis或者HBase都是有可能的！
		studentsDF.javaRDD().foreach(new VoidFunction<Row>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Row row) throws Exception {
				String sql = "insert into good_student_infos values("
						+ "'" + row.getString(0) + "',"
						+ Integer.valueOf(String.valueOf(row.get(1))) + ","
						+ Integer.valueOf(String.valueOf(row.get(2))) + ")" ;
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = null;
				Statement stat = null;
				try {
					conn = DriverManager.getConnection("jdbc:mysql://spark001:3306/test","root","123123");
					stat = conn.createStatement();
					stat.executeUpdate(sql);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(stat != null){
						stat.close();
					}
					if(conn != null){
						conn.close();
					}
				}
			}
		});
	}
}
