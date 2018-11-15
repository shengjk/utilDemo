package util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;

/**
 * Created by 小省  on 2016/6/13.
 */
public class InsertHbase {
	private Logger logger=Logger.getLogger(InsertHbase.class);
	public void insert(String rowkey, String value)throws Exception{
		Table table=null;
		Connection conn=null;
		try {

			long c = System.currentTimeMillis();
			//rowkey: phone_num_day
			Configuration config = HBaseConfiguration.create();
			//即使连错zk 也不会报错
			config.set("hbase.zookeeper.quorum", "1111");
			config.set("hbase.zookeeper.property.clientPort", "2181");
			
			conn= ConnectionFactory.createConnection(config);
			String table_name="dataresource_rawdata";
			table=conn.getTable(TableName.valueOf("xxx_datalogic:"+table_name));

			String qualifier="f";


			Put put =new Put(rowkey.getBytes());
			put.addColumn("content".getBytes(), qualifier.getBytes(), value.getBytes());


			table.put(put);

			long b = System.currentTimeMillis();
			if (b-c>3000){
				logger.error("insert hbase 过慢======== "+(b-c) +" 毫秒");
			}
		}finally {
			table.close();
			conn.close();
		}
	}

	public static void main(String[] args) {
		new InsertHbase();


	}
}
