package util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Created by shengjk1 on 2017/3/29 0029.
 */
public class JdbcImpala {
	public static void main(String[] args) {
		impalConn();
	}
	
	
	public static void impalConn() {
		long c = System.currentTimeMillis();
		
		Statement stmt = null;
		Connection conn = null;
		
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
			String url = "jdbc:hive2://192.168.0.232:21050/;auth=noSasl";
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			stmt.execute("INVALIDATE METADATA");
			
//			List<String> list = Test2.readFile("I:\\gitProject\\test\\file\\table");
			List<String> list=null;
			ResultSet rs=null;
			for (String tableName:list) {
				rs=stmt.executeQuery("select COUNT (*) from dws_promisechina.s_"+tableName);
				System.out.println("impala查询完成 ======>>  tableName "+tableName +"  个数 "+rs.getInt(1));
				Thread.currentThread().sleep(30);
			}
			
			
			
			
		}catch (Exception e){
			System.out.println( e);
		}
	}
	
}
