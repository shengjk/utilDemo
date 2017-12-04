package util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by shengjk1 on 2016/9/9.
 */
public class Jdbc {
	public static void main(String[] args) throws Exception {
		Select();
	}
	
	
	public static void Update() throws Exception {
		String driver ="com.mysql.jdbc.Driver";
		String url ="jdbc:mysql://192.168.148.128:3306/test";
		String user="root";
		String password ="123456";
		//加载驱动
		Class.forName(driver);
		//获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		
		Statement stmt =conn.createStatement();
		int age=28;
		while (true){
			age+=1;
			String sql="update tb_user set age='"+age+"' where id=2";
			System.out.println(sql); //方便排除错误
			int result =stmt.executeUpdate(sql);
			
			System.out.println(result>0?"成功":"失败");
		}
//
//		stmt.close();
//		conn.close();
		
	}
	
	
	
	public static void Select() throws Exception {
		String driver ="com.mysql.jdbc.Driver";
		String url ="jdbc:mysql://192.168.149.128:3306/test";
		String user="root";
		String password ="123456";
		//加载驱动
		Class.forName(driver);
		//获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		
		Statement stmt =conn.createStatement();
		int age=28;
		while (true){
			
			String sql="SELECT age from users where id =2;";
			System.out.println(sql); //方便排除错误
			ResultSet rs=stmt.executeQuery(sql);
			
			if(rs.next()){
				System.out.println("结果 "+rs.getString(1));
			}
			
		}
//
//		stmt.close();
//		conn.close();
		
	}
	
}
