package util.jdbc;


import util.config.ConfigurationManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {

	// 队列
	private static LinkedList<Connection> connectionQueue;

	static {
		// 加载驱动
		try {
			String diver= ConfigurationManager.getProperty("jdbc.driver");
			Class.forName(diver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public synchronized static Connection getConnection() {
		try {
			if (connectionQueue == null) {
				connectionQueue = new LinkedList<Connection>();
				String jdbc= ConfigurationManager.getProperty("jdbc.url");
				String user= ConfigurationManager.getProperty("jdbc.user");
				String pw= ConfigurationManager.getProperty("jdbc.password");
				int count= ConfigurationManager.getInteger("jdbc.datasource.size");
				
				for (int i = 0; i < count; i++) {
					Connection conn = DriverManager.getConnection(
							jdbc, user,pw);
					connectionQueue.push(conn);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connectionQueue.poll();
	}
	
	public static void returnConnection(Connection conn){
		connectionQueue.push(conn);
	}
}
