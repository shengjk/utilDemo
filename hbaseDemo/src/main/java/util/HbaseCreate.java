package util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression;


/**
 * Created by shengjk1 on 2016/9/26.
 */
public class HbaseCreate {
	
	public void create(String table_name,String cf)throws Exception{
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", "datanode1,datanode2,datanode3");
		
		Connection conn= ConnectionFactory.createConnection(config);
		
		Admin admin=conn.getAdmin();
//
//		if(admin.tableExists(TableName.valueOf(table_name)) && admin.isTableEnabled(TableName.valueOf(table_name))){
//			admin.disableTable(TableName.valueOf(table_name));
//			admin.deleteTable(TableName.valueOf(table_name));
//		}
		HTableDescriptor table =new HTableDescriptor(TableName.valueOf(table_name.getBytes()));
		HColumnDescriptor cf1= new HColumnDescriptor(cf.getBytes());
		//设置存储时压缩
		cf1.setCompressionType(Compression.Algorithm.SNAPPY);
		
		table.addFamily(cf1);
		
		admin.createTable(table);
		System.out.println("over! ");
		admin.close();
		conn.close();
	}
}
