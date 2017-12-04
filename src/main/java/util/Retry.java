package util;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by shengjk1 on 2017/8/15
 */
public class Retry {
	private final static Logger log = Logger.getLogger("Retry.class");
	/**
	 *retry job if job error
	 * @param jobId
	 * @return
	 */
	protected  static String retryGetJobStatus(String jobId){
		int retry =0;
		String	JobStatus="";
		while (retry <3){
			try {
				System.out.println("需要重试的");
				break;
			} catch (Exception e) {
				log.error("retryException "+e);
				retry +=1;
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e1) {
					log.error("InterruptedException "+e);
				}
				continue;
			}
		}
		return JobStatus;
	}
	
}
