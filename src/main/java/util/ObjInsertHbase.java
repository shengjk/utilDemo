package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;
import util.config.ConfigurationManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 小省  on 2016/6/13.
 */
public class ObjInsertHbase {
	private static Logger logger = Logger.getLogger(ObjInsertHbase.class);
	
	/**
	 * @param mapObj <rowkey,Object>
	 * @param table_name
	 * @throws Exception
	 */
	public static void insert(Map<String,Object>mapObj, String table_name) throws Exception {
		if (mapObj.size() < 1 || StringUtils.isBlank(table_name)) {
			throw new RuntimeException("ObjInsertHbase insertHbase 参数不符合要求: mapObj.size() .size():" + mapObj.size()  + "  table_name:" + table_name);
		}
		
		Table table = null;
		Connection conn = null;
		try {
			
			long c = System.currentTimeMillis();
			
			String zkquorum = ConfigurationManager.getProperty("hbase.zookeeper.quorum");
			
			Configuration config = HBaseConfiguration.create();
			
			
			config.set("hbase.zookeeper.quorum", zkquorum);
			conn = ConnectionFactory.createConnection(config);
			table = conn.getTable(TableName.valueOf(table_name));
			
			List<Put> putList=new ArrayList();
			
			if (mapObj.keySet().size()<1 || mapObj.values().size()<1){
				logger.error("scanError ObjInsertHbase insert hbase传入map有误");
				throw new RuntimeException("scanError ObjInsertHbase insert hbase传入map有误");
			}
			
			for (String key : mapObj.keySet()) {
				
				
				Put put = new Put(key.getBytes());
				Object obj=mapObj.get(key);
				
				if (null==key|| null==obj){
					throw new RuntimeException("ObjInsertHbase 数据错误 rowkey :"+key +" obj: "+obj);
				}
				
				/**
				 * 获取对象中的每一个字段
				 */
				Field[] fields = obj.getClass().getDeclaredFields();
				for (int i = 0, len = fields.length; i < len; i++) {
					
					String varName = fields[i].getName();
					boolean accessFlag = fields[i].isAccessible();
					fields[i].setAccessible(true);
					Object o = fields[i].get(obj) == null ? " " : fields[i].get(obj);
					fields[i].setAccessible(accessFlag);
					
					if (!varName.equalsIgnoreCase("serialVersionUID") && StringUtils.isNotBlank(varName)) {
						logger.info("ObjInsertHbase varName " + varName + "   o " + o);
						put.addColumn("f".getBytes(), varName.getBytes(), o.toString().getBytes());
					} else {
						logger.error("scanError ObjInsertHbase varName 属性名获取失败");
						throw new RuntimeException("scanError ObjInsertHbase varName 属性名获取失败");
					}
					
					putList.add(put);
				}
			}
			table.put(putList);
			
			
			long b = System.currentTimeMillis();
			
			logger.info("ObjInsertHbase insert hbase ======== " + (b - c) + " 毫秒");
		} finally {
			table.close();
			conn.close();
		}
	}
	
}
