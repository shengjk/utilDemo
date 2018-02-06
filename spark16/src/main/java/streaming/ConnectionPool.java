package streaming;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

/**
 * Created by shengjk1 on 2016/8/11.
 */
public class ConnectionPool {
	private static LinkedList<Connection> connQueue;
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static Connection getConnection()	{
		try {
			
			if (connQueue == null) {
				connQueue = new LinkedList<>();
				for (int i = 0; i < 5; i++) {
					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://spark001:3306/test", "root",
							"123123");
					connQueue.push(conn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connQueue.poll();
	}
	public static void retrunConneection(Connection conn){
		connQueue.push(conn);
	}
}
