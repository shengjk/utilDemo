package streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by shengjk1 on 2016/8/11.
 */
public class PersistMySQLWordcount {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("PersistMySQLWordcount")
				.setMaster("local");
		JavaStreamingContext ssc =new JavaStreamingContext(conf, Durations.seconds(5));
		
		JavaDStream<String>lines= ssc.textFileStream("hdfs://10.16.30.54:8020/test");
		lines.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1747521428173312204L;
			
			@Override
			public Iterable<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" "));
			}
		});
		
		JavaPairDStream<String,Integer> line=lines.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = -1162182675196544115L;
			
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s,1);
			}
		});
		JavaPairDStream<String,Integer>res=line.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		});
		res.print();
		res.foreachRDD(new VoidFunction<JavaPairRDD<String, Integer>>() {
			@Override
			public void call(JavaPairRDD<String, Integer> stringIntegerJavaPairRDD) throws Exception {
				stringIntegerJavaPairRDD.foreachPartition((VoidFunction<Iterator<Tuple2<String, Integer>>>) tuple2Iterator -> {
					Connection conn= DriverManager.getConnection("jdbc:mysql://192.168.2.23:3306/test", "root","hadoop");
					Statement st =conn.createStatement();
					
					Tuple2<String,Integer> tup=null;
					while (tuple2Iterator.hasNext()){
						tup=tuple2Iterator.next();
						String sql = "insert into wordcount(word,count) "
								+ "values('" + tup._1 + "'," + tup._2 + ")";
						st.execute(sql);
					}
				});
			}
		});
		
		
		
		ssc.start();
		ssc.awaitTermination();
		ssc.stop();
		
	}
}
