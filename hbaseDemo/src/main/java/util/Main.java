package util;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;

/**
 * Created by 小省  on 2016/5/24.
 */
public class Main {
	private static Logger logger=Logger.getLogger(Main.class);

	@Test
	public  void insert1()throws Exception{
		long c = System.currentTimeMillis();
		//rowkey: phone_num_day
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "10.16.30.54,10.16.30.56,10.16.30.55");
		Connection conn= ConnectionFactory.createConnection(config);
		String table_name="test";
		Table table=conn.getTable(TableName.valueOf(table_name));

		String rowkey="123456789_GAA01_buscode_reportId";
		String qualifier="f";

//		String fileValue = FileUtils.readFileToString(new File(path));

		Put put =new Put(rowkey.getBytes());
		put.addColumn("f".getBytes(), qualifier.getBytes(), "sbsbsbsbsb".getBytes());


		table.put(put);

		long b = System.currentTimeMillis();

		System.out.println("======== "+(b-c));
		table.close();
		conn.close();
	}

	public static void main(String[] args) throws Exception {
//		String path=args[0];
//		for(int i=0;i<10000;i++){
//
//			long c= System.currentTimeMillis();
//			insert1(path);
//			long b =System.currentTimeMillis();
//			logger.info("b-c  "+(b-c)+" ms");
//		}

		String a =Main.class.getResource("/")+"ne12441435123";
		a=a.replace("file:/","");
		System.out.println(a);
		System.out.println(FileUtils.readFileToString(new File(a)));
		System.out.println(Main.class.getClassLoader().getResource("ne12441435123"));
		//file:/D:/gitProject/hbase/target/classes/xxx/
		System.out.println("Main.class.getResource(\"\") "+Main.class.getResource(""));
		//file:/D:/gitProject/hbase/target/classes/
		System.out.println("Main.class.getResource(\"/\") "+Main.class.getResource("/"));
//		file:/D:/gitProject/hbase/target/classes/
		System.out.println("Main.class.getResource(\"/\") "+Main.class.getClassLoader().getResource(""));
		//D:\gitProject\hbase
		System.out.println("System.getProperty(\"user.dir\") "+System.getProperty("user.dir"));
	}
}
