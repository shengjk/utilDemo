package other;

import org.slf4j.LoggerFactory;

/**
 * Created by shengjk1 on 2017/12/4
 */
public class Demo {
	protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(Demo.class);
	
	public static void main(String[] args) {
		String a=Thread.currentThread().getContextClassLoader().getResource("").getFile()+ "conf.properties";
		System.out.println(a);
		
		/**
		 *  ==== Thread[main,5,main]
		 ==== sun.misc.Launcher$AppClassLoader@18b4aac2
		 ==== file:/Users/iss/sourceCode/gitProject/iss/data_exchange/dataexchange/target/classes/conf.properties
		 ==== /Users/iss/sourceCode/gitProject/iss/data_exchange/dataexchange/target/classes/conf.properties
		 */
		
		logger.info("==== "+Thread.currentThread());
		logger.info("==== "+Thread.currentThread().getContextClassLoader());
		logger.info("==== "+Thread.currentThread().getContextClassLoader().getResource("conf.properties"));
		logger.info("==== "+Thread.currentThread().getContextClassLoader().getResource("conf.properties").getFile());
		
//		泛型
//		List<?> plugins = root.elements("plugin");
	}
}
