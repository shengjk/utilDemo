package javasssss;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;

import java.util.Iterator;

/**
 * Created by shengjk1 on 2016/8/8.
 */
public class SparkInsertHbase {
	
	public static void main(String[] args) {
//		System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
//		System.setProperty("spark.kryo.classesToRegister", "org.apache.hadoop.io.Text");
//
		SparkConf conf = new SparkConf().setAppName("SparkInsertHbase");
//		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
//		conf.set("spark.kryo.registrator", "javasssss.SparkInsertHbase");
//		conf.set("spark.kryo.classesToRegister", "org.apache.hadoop.io.Text");
		JavaSparkContext sc = new JavaSparkContext(conf);
		HiveContext hiveContext = new HiveContext(sc.sc());
		
		
		DataFrame df = hiveContext.sql("select id,name from test");
//			df.show();
		
		
		//froeachPartition  foreah 回报task not  to  serializer
		df.toJavaRDD().foreachPartition(new VoidFunction<Iterator<Row>>() {
			
			
			private static final long serialVersionUID = -3496935835002029475L;
			
			@Override
			public void call(Iterator<Row> rowIterator) throws Exception {
//				HTable table = new HTable(HBaseConfiguration.create(), "test");
				Configuration config = HBaseConfiguration.create();
				//即使连错zk 也不会报错
				config.set("hbase.zookeeper.quorum", "centos2");
				Connection conn= ConnectionFactory.createConnection(config);
				Table table=conn.getTable(TableName.valueOf("test"));
				
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					String id = row.getString(0);
					String name = row.getString(1);
					Put put = new Put("f".getBytes());
					put.addColumn("f".getBytes(), "id".getBytes(), id.getBytes());
					put.addColumn("f".getBytes(), "name".getBytes(), name.getBytes());
					
					table.put(put);
				}


//				String tableName = "test";
//				Table table=conn.getTable(TableName.valueOf(tableName));
				
				
			};
			
		});
		
		
	}
	
}
