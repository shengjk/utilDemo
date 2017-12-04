package util.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理组件
 *
 * @author Administrator
 */
public class ConfigurationManager {
	protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
	
	private static Properties prop = new Properties();
	
	static {
		try {
			// 通过一个“类名.class”的方式，就可以获取到这个类在JVM中对应的Class对象
			// 然后再通过这个Class对象的getClassLoader()方法，就可以获取到当初加载这个类的JVM
			// 中的类加载器（ClassLoader），然后调用ClassLoader的getResourceAsStream()这个方法
			// 就可以用类加载器，去加载类加载路径中的指定的文件
			// 最终可以获取到一个，针对指定文件的输入流（InputStream）
			InputStream in = ConfigurationManager.class
					.getClassLoader().getResourceAsStream("conf.properties");
			
			// 调用Properties的load()方法，给它传入一个文件的InputStream输入流
			// 即可将文件中的符合“key=value”格式的配置项，都加载到Properties对象中
			// 加载过后，此时，Properties对象中就有了配置文件中所有的key-value对了
			// 然后外界其实就可以通过Properties对象获取指定key对应的value
			prop.load(in);
		} catch (Exception e) {
			logger.error(""+e);
		}
	}
	
	/**
	 * @param key
	 * @return value
	 */
	public static String getProperty(String key) {
		return StringUtils.trim(prop.getProperty(key));
	}
	
	/**
	 * 获取整数类型的配置项
	 *
	 * @param key
	 * @return value
	 */
	public static Integer getInteger(String key) {
		String value = getProperty(key);
		return Integer.valueOf(value);
	}
	
	/**
	 * 获取布尔类型的配置项
	 *
	 * @param key
	 * @return value
	 */
	public static Boolean getBoolean(String key) {
		String value = getProperty(key);
		return Boolean.valueOf(value);
	}
	
	/**
	 * 获取Long类型的配置项
	 *
	 * @param key
	 * @return
	 */
	public static Long getLong(String key) {
		String value = getProperty(key);
		return Long.valueOf(value);
	}
}


