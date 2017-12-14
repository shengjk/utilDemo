package util.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by shengjk1 on 2017/12/11
 */
public class ConfigManager {
	protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigManager.class);
	
	private boolean autoReload;
	
	private static final String BASE_PATH;
	
	private static final String GLOBAL_CONFIG_PATH = "global.config.path";
	
	private static final String fileName="conf.properties";
	
	private static volatile PropertiesConfiguration prop =null;
	
	static{
		logger.info("开始初始化ConfigurationManager===================== ");
		SystemConfiguration sysConfig = new SystemConfiguration();
		String globalPath = sysConfig.getString(GLOBAL_CONFIG_PATH);
		logger.info("globalPath=======================  "+globalPath);
		if(StringUtils.isBlank(globalPath)){/**默认加载classpath下面的文件**/
			globalPath = Thread.currentThread().getContextClassLoader().getResource("conf.properties").getFile();
		}
		BASE_PATH=globalPath;
	}
	
	public ConfigManager(boolean autoReload) throws ConfigurationException {
		this.autoReload=autoReload;
		loadConfig();
	}
	
	public void loadConfig() throws ConfigurationException {
		if (null==prop){
			prop=new PropertiesConfiguration();
		}
		File file=new File(BASE_PATH);
		prop.setFile(file);
		prop.setAutoSave(false);
		if(autoReload){/**重载策略,5秒钟监视文件变化***/
			prop.setReloadingStrategy(new FileChangedReloadingStrategy());
		}
		prop.load();
		
		
	}
	
	
	/**
	 * @param key
	 * @return value
	 */
	public  String getProperty(String key) {
		return prop.getString(key);
	}
	
	/**
	 * 获取整数类型的配置项
	 *
	 * @param key
	 * @return value
	 */
	public  Integer getInteger(String key) {
		String value = getProperty(key);
		return Integer.valueOf(value);
	}
	
	/**
	 * 获取布尔类型的配置项
	 *
	 * @param key
	 * @return value
	 */
	public  Boolean getBoolean(String key) {
		String value = getProperty(key);
		return Boolean.valueOf(value);
	}
	
	/**
	 * 获取Long类型的配置项
	 *
	 * @param key
	 * @return
	 */
	public  Long getLong(String key) {
		String value = getProperty(key);
		return Long.valueOf(value);
	}
	
	
	private static class SingletonHelp {
		static ConfigManager instance;
		static {
			try {
				instance = new ConfigManager(true);
			} catch (ConfigurationException e) {
				logger.error("ConfigurationManager error" +e);
			}
		}
	}
	
	
	public static ConfigManager build(){
		return SingletonHelp.instance;
	}
	
	
	
	
	public static void main(String[] args) throws InterruptedException, ConfigurationException {
		ConfigManager configManager =build();
		logger.info("==== "+Thread.currentThread());
		logger.info("==== "+Thread.currentThread().getContextClassLoader());
		logger.info("==== "+Thread.currentThread().getContextClassLoader().getResource("conf.properties"));
		logger.info("==== "+Thread.currentThread().getContextClassLoader().getResource("conf.properties").getFile());
		
	}
}


