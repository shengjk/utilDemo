package util.logDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by shengjk1 on 2017/11/27
 */
public class LoTest {
	private static final Logger logger = LoggerFactory.getLogger(LoTest.class);
	
	public static void main(String[] args) {
		
		MDC.put("THREAD_ID", String.valueOf(Thread.currentThread().getId()));
		
		logger.info("纯字符串信息的info级别日志");
		
//		logger.info("结果：{}", args);
//		logger.error("rollback,batchId %d  ,error %s ",batchId,e);
	}
	
}
