package javasql;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小省 on 2016/7/5.
 */
public class JDBCDataSource {
	public static void main(String[] args) throws ConfigurationException {
		SparkConf conf =new SparkConf().setAppName("JDBCDataSource").setMaster("local")
				;
		JavaSparkContext sc =new JavaSparkContext(conf);
		SQLContext sqlContext=new SQLContext(sc);
		
		PropertiesConfiguration pro =new PropertiesConfiguration("mysql.properties");
		
		String url=pro.getString("url");
				
		Map<String,String> option= new HashMap<>();
		option.put("url",url);
		option.put("dbtable","score");
		option.put("user","root");
		option.put("password","123456");

	DataFrame studentScoresDF=sqlContext.read().format("jdbc").options(option).load();

		option.put("dbtable","student");
		DataFrame studentInfosDF = sqlContext.read().format("jdbc")
				.options(option).load();

		/*
		 读取字段顺序，跟mysql中表的字段顺序是一致的
	    */
		JavaPairRDD<String, Tuple2<Integer, Integer>> studentsRDD = studentInfosDF.javaRDD().mapToPair(x -> new Tuple2<String, Integer>(
				x.getString(0), Integer.valueOf(String.valueOf(x.get(1)))))
				.join(
						studentScoresDF.javaRDD().mapToPair(x -> new Tuple2<String, Integer>(
								x.getString(0), Integer.valueOf(x.getInt(1))
						))
				);
		JavaRDD<Row> studentsRowRDD = studentsRDD.map(x -> RowFactory.create(x._1, x._2._1, x._2._2))

				.filter(x -> Integer.valueOf(String.valueOf(x.get(2))) > 87);

//schme
		List<StructField> fileds=new ArrayList<>();
		fileds.add(DataTypes.createStructField("name", DataTypes.StringType,true));
		fileds.add(DataTypes.createStructField("age", DataTypes.IntegerType,true));
		fileds.add(DataTypes.createStructField("score",DataTypes.IntegerType,true));

		StructType schema = DataTypes.createStructType(fileds);
		DataFrame studentsDF = sqlContext.createDataFrame(studentsRowRDD, schema);


		Row[] row=studentsDF.collect();
		
		for (Row row1 : row) {
			System.out.println(row1);

		}
//		//foreachPatition
//		studentsDF.javaRDD().foreach(new VoidFunction<Row>() {
//			@Override
//			public void call(Row row) throws Exception {
//				String sql = "insert into good_student values("
//						+ "'" + row.getString(0) + "',"
//						+ Integer.valueOf(String.valueOf(row.get(1))) + ","
//						+ Integer.valueOf(String.valueOf(row.get(2))) + ")" ;
//				Class.forName("com.mysql.jdbc.Driver");
//				Connection conn = null;
//				Statement stat = null;
//				try {
//					conn = DriverManager.getConnection("jdbc:mysql://centos1:3306/test","root","123456");
//					stat = conn.createStatement();
//					stat.executeUpdate(sql);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					if(stat != null){
//						stat.close();
//					}
//					if(conn != null){
//						conn.close();
//					}
//				}
//
//			}
//		});

		//一个partition 一提交
//	studentsDF.javaRDD().foreachPartition(new VoidFunction<Iterator<Row>>() {
//		@Override
//		public void call(Iterator<Row> rowIterator) throws Exception {
//
//		}
//	});

	}
}
