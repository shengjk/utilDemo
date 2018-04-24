package util.jdbc;

import org.apache.log4j.Logger;

import java.sql.*;


public class ImpalaJdbc {
	private static Logger logger = Logger.getLogger(ImpalaJdbc.class);
	private static long c =System.currentTimeMillis();
	public static void connImpala() {
//		Statement stmt = null;
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
//			does not use Kerberos authentication
			conn = DriverManager
					.getConnection("jdbc:hive2://10.16.30.57:21050/;auth=noSasl");
			stmt = conn.createStatement();
			
			long a =System.currentTimeMillis();
			//未首次逾期    未首次逾期一定未首次还款
			String nSql="insert into foreign.foreign_sparklesilver select ac.APPLICATION_NO,ac.CONTR_NBR,'0' first_overdue,'0'first_repayment, '' from dsc_dws_test.dws_i_ccs_ccs_acct ac"
+" left join dsc_dws_test.dws_i_ccs_ccs_loan cl on ac.ACCT_NBR=cl.ACCT_NBR"
+" where   ac.ACCT_NBR not in(select ACCT_NBR from foreign.firstoverdue)" 
+" and cl.OVERDUE_DATE is null"
+" and LOAN_CODE='1301' "
+" and ac.`date`=from_unixtime(unix_timestamp(now()),'yyyyMMdd')"
+" and cl.`date`=from_unixtime(unix_timestamp(now()),'yyyyMMdd')";
//			stmt.execute(nSql);
			System.out.println("nSql "+nSql);
			ResultSet rs1=stmt.executeQuery("select count(*)a from foreign.foreign_sparklesilver");
			int sumCount=0;
			if(rs1.next()){
				sumCount=rs1.getInt("a");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				long d =System.currentTimeMillis();
				System.out.println("=================="+(d-c)/1000);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		String confPath="conf";
		new ImpalaJdbc().connImpala();
	}
}
