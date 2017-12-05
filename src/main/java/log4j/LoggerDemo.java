package log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shengjk1 on 2017/12/5
 */
public class LoggerDemo {
	protected final static Logger logger = LoggerFactory.getLogger(LoggerDemo.class);
	
	
	public static void main(String[] args) {
		String result="aa";
		logger.info("结果：{}", result);
		logger.info("结果：{}, haha {} ", result,result);
	}
}
