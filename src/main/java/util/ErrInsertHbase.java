package util;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
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
public class ErrInsertHbase {
	private static Logger logger = Logger.getLogger(ErrInsertHbase.class);
	
	/**
	 *
	 * @param rowkey  type+时间戳
	 * @param value
	 * @param table_name
	 * @throws Exception
	 */
	public static void insert(String rowkey, String value, String table_name) throws Exception {
		
		if (StringUtils.isBlank(rowkey) || StringUtils.isBlank(table_name)||StringUtils.isBlank(value)) {
			throw new RuntimeException("ErrInsertHbase   insertHbase 参数不符合要求: rowkey:" +rowkey  + "  table_name:" + table_name+" value:"+value);
		}
		

		Table table = null;
		Connection conn = null;
		try {
			long c = System.currentTimeMillis();
			
			PropertiesConfiguration pro = new PropertiesConfiguration("conf.properties");
			String zkquorum = pro.getString("hbase.zookeeper.quorum");
			
			Configuration config = HBaseConfiguration.create();
			//即使连错zk 也不会报错
			config.set("hbase.zookeeper.quorum", zkquorum);
			conn = ConnectionFactory.createConnection(config);
			table = conn.getTable(TableName.valueOf(table_name));
			
			
			Put put = new Put(rowkey.getBytes());
			put.addColumn("f".getBytes(), "error".getBytes(), value.getBytes());
			
			
			table.put(put);
			
			long b = System.currentTimeMillis();
			logger.info("ErrInsertHbase insert hbase ======== " + (b - c) + " 毫秒");
			
		} finally {
			table.close();
			conn.close();
		}
	}
	
}
