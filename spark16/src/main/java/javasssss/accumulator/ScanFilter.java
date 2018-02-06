package javasssss.accumulator;

import org.apache.log4j.Logger;
import org.apache.spark.Accumulator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shengjk1 on 2016/11/4.
 */
public class ScanFilter {
	private static Logger logger = Logger.getLogger(ScanFilter.class);

//	public static Boolean filter(Accumulator<Set<String>> filter,Accumulator<Set<String>> sum, String v1, final Integer length, final String hbaseErrorTable){
//		String lines[] = v1.replace("\"", "").replace("\'", "").split("\\|", -1);
//
//		//scantime  ecode
//		if (lines.length == length && lines[10].trim().length() > 0 && lines[4].trim().length() > 0 &&null!=lines[10] &&!"null".equalsIgnoreCase(lines[10].trim())
//				&&null!=lines[4] &&!"null".equalsIgnoreCase(lines[4].trim())) {
//			Boolean bool=false;
//			Connection connection= ConnectionPool.getInstance().getConnection();
//			try {
//				connection.setAutoCommit(false);
//				String FilterId=lines[0]+"_"+lines[1]+"_"+lines[2];
//				String scanFlitertable=ConfigurationManager.getProperty("Scan_Filter_table");
//				String sql="select date from "+scanFlitertable+" where id=\'"+FilterId+"\'";
//				PreparedStatement pstmt=connection.prepareStatement(sql);
//				ResultSet res=pstmt.executeQuery();
//				if (res.next()){
//					//过滤掉
//					bool= false;
//				}else {
//					//一个一个的过滤     为保证数据execute one 此处不适宜多线程
//					String sql1="insert into "+scanFlitertable+" values( \'"+FilterId+"\',\'"+ DateUtils.getTodayDate()+"\')";
//					logger.info("scan ScanFilter sql1 "+sql1);
//					pstmt=connection.prepareStatement(sql1);
//					pstmt.executeUpdate();
//					Set<String> set=new HashSet<>();
//					set.add(FilterId);
//					sum.add(set);
//					logger.info("ConfigurationManager.scanFliterList size=============== "+sum);
//					bool= true;
//				}
//				connection.commit();
//			} catch (SQLException e) {
//				try {
//					logger.error("scanError ScanFilter  ERROR:" + e);
//					SendEmailUtils.send("scanError ScanFilter", "scanError  ScanFilter " + e);
//					connection.rollback();
//					System.exit(1);
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//			}finally {
//				ConnectionPool.returnConnection(connection);
//			}
//			return bool;
//		} else {
//			try {
//				if (StringUtils.isNotBlank(v1)) {
//					ErrInsertHbase.insert(System.currentTimeMillis()+"", v1, hbaseErrorTable);
//					logger.info("scanError ErrInsertHbase 日志格式错误 serviceId:" + lines[0] + " v1: " + v1);
//				} else {
//					logger.info("scanError ErrInsertHbase 日志内容 v1:" + v1);
//				}
//			} catch (Exception e) {
//				logger.error("scanError ErrInsertHbase  ERROR:" + e);
//				SendEmailUtils.send("====scanError ==== insertHbase", "====scanError ==== insertHbase " + e);
//			}
//			return false;
//		}
//
//	}
	
	
	public static Boolean filter(Accumulator<Set<String>> filter, String v1, final Integer length, final String hbaseErrorTable) {
		String lines[] = v1.replace("\"", "").replace("\'", "").split("\\|", -1);
		
		//scantime  ecode
		if (lines.length == length && lines[10].trim().length() > 0 && lines[4].trim().length() > 0 && null != lines[10] && !"null".equalsIgnoreCase(lines[10].trim())
				&& null != lines[4] && !"null".equalsIgnoreCase(lines[4].trim())) {
			String FilterId = lines[0] + "_" + lines[1] + "_" + lines[2];
			Set<String> set = filter.localValue();
			logger.info("==============================================================");
			logger.info("======================= set "+set.isEmpty());
			logger.info("======================= set "+set.size());
			logger.info("==============================================================");
			//不能用filter相关的set
			Set<String> set2 = new HashSet<>();
			//bool true not contains 不过滤  flase  contains   过滤
			Boolean bool = !set.contains(FilterId);
			if (bool) {
				set2.add(FilterId);
				filter.add(set2);
				bool=true;//留下
			}else {
				bool=false;
			}
			System.out.println("==============================true ");
			return bool;
		} else {
			System.out.println("==============================else ");
			return false;
		}
		
		/**
		 * 累加器应该是分合之势，首先是根据partition也就是task的数目，以及定义的Accumulator，
		 * 产生相应数量的accumulator,当程序跑完时，或者该批次跑完时，spark会把这些分散的accumulator进行合并
		 *
		 * sum.value()是一个set
		 *当sum.value().clear()时清空的是初始值
		 *
		 * 两条记录
		 *  utils.ScanFilter: ======================= set true
		 16/12/15 18:13:01 INFO utils.ScanFilter: ======================= set 0
		 16/12/15 18:13:01 INFO utils.ScanFilter: ==============================================================
		 16/12/15 18:13:01 INFO spark.CacheManager: Partition rdd_606_1 not found, computing it
		 16/12/15 18:13:01 INFO kafka.KafkaRDD: Computing topic bdscanlog3, partition 0 offsets 356753 -> 356754
		 16/12/15 18:13:01 INFO utils.VerifiableProperties: Verifying properties
		 16/12/15 18:13:01 INFO utils.VerifiableProperties: Property auto.offset.reset is overridden to smallest
		 16/12/15 18:13:01 INFO utils.VerifiableProperties: Property group.id is overridden to
		 16/12/15 18:13:01 INFO utils.VerifiableProperties: Property zookeeper.connect is overridden to
		 16/12/15 18:13:01 INFO storage.MemoryStore: Block rdd_606_1 stored as values in memory (estimated size 416.0 B, free 831.8 KB)
		 16/12/15 18:13:01 INFO storage.BlockManagerInfo: Added rdd_606_1 in memory on localhost:34201 (size: 416.0 B, free: 530.0 MB)
		 16/12/15 18:13:01 INFO utils.ScanFilter: ==============================================================
		 16/12/15 18:13:01 INFO utils.ScanFilter: ======================= set false
		 16/12/15 18:13:01 INFO utils.ScanFilter: ======================= set 1
		 16/12/15 18:13:01 INFO utils.ScanFilter: =================================== set scan 02-42-AC-11-00-09_1481267157778_1
		 16/12/15 18:13:01 INFO utils.ScanFilter: ==============================================================
		 
		 */
	}
}
