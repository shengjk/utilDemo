package util.config;

import org.apache.commons.configuration.*;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/** 
 * 动态加载配置文件类 
 * <pre> 
 * 默认为动态加载配置,当然你也可以指定autoReload=false不动态更新配置 
 * 配置信息来源： 
 * 1.JVM启动参数 
 * 2.系统环境变量 
 * 3.classpath下面的.properties配置文件(没有在JVM启动参数中配置-Dglobal.config.path="配置分离目录"时的加载策略) 
 * 4.global.config.path(配置分离目录)下面的所有的.properties配置文件(注：需要在JVM启动参数中配置-Dglobal.config.path="配置分离目录") 
 * 5.${global.config.path}/${appConfigPath}下面的所有的.properties配置文件(注：需要在JVM启动参数中配置-Dglobal.config.path="配置分离目录"和传入appConfigPath参数) 
 * 6.${global.config.path}/${appConfigPath}下面特定的.properties配置文件(注：需要在JVM启动参数中配置-Dglobal.config.path="配置分离目录"和传入appConfigPath参数、configFiles配置文件列表(多个配置文件用,分隔)) 
 * 说明：1,2是自动加入的,无需配置. 3与4,5,6是互斥的,当没有在JVM启动参数中配置-Dglobal.config.path="配置分离目录"时,就使用3,反之就用4,5,6而不会加载classpath下面的配置文件 
 * </pre> 
 * <strong>使用方法</strong> 
 * <pre> 
 * eg:创建一个自动动态加载${global.config.path}/openapi-service/目录下面的mail.properties,common.properties,sysConfig.properties配置文件的对象 
 * PropertyPlaceholderConfigurer config = PropertyPlaceholderConfigurer.getBuilder().autoReload(true).appConfigPath("openapi-service").configFiles("mail.properties,common.properties").configFiles(new String [] {"sysConfig.properties"}).build(); 
 * </pre> 
 * <strong>lib依赖</strong> 
 * @since commons-configuration 1.8 
 * @since commons-lang 2.6 
 * @author zhangjiawei 2012-7-20 
 * 
 */  
public final class PropertyPlaceholderConfigurer {
	
	/**配置分离基目录**/
    private static final String BASE_PATH;  
      
    /**应用程序在配置分离下面的子目录**/  
    private String appConfigPath = "";  
      
    /**要加载的配置文件,多个配置文件用,分隔,不写则加载目录下面的所有.properties文件**/  
    private String [] configFiles;  
      
    /**自动动态加载,默认为true**/  
    private boolean autoReload = true;  
      
    /**properties文件配置项**/  
    private final CompositeConfiguration props = new CompositeConfiguration();
      
    /**配置文件对应的PropertiesConfiguration列表**/  
    private List<PropertiesConfiguration> propList = new ArrayList<PropertiesConfiguration>(10);
      
    /**全局配置分离参数**/  
    private static final String GLOBAL_CONFIG_PATH = "global.config.path";
    
    static{  
        /**JVM启动参数配置***/  
        SystemConfiguration sysConfig = new SystemConfiguration();
        String globalPath = sysConfig.getString(GLOBAL_CONFIG_PATH);  
        if(StringUtils.isBlank(globalPath)){/**默认加载classpath下面的文件**/  
            globalPath = Thread.currentThread().getContextClassLoader().getResource("").getFile();  
        }  
        BASE_PATH = globalPath;  
    }  
  
    /** 
     * 实例完成对象后,初始化操作 
     */  
    private void init(){  
        /**加入配置JVM的启动参数**/  
        props.addConfiguration(new SystemConfiguration());
        /**加入环境变更参数**/  
        props.addConfiguration(new EnvironmentConfiguration());
    }  
      
      
    /** 
     * 加载默认的配置文件： 
     * 如果加了启动参数:global.config.path,就会加载global.config.path下面所有的.properties配置文件 
     * 如果没有,就加载classpath下面所有的.properties配置文件 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer() throws ConfigurationException {
        this(true,null,"");  
    }  
      
    /** 
     * 加载默认的配置文件： 
     * 如果加了启动参数:global.config.path,就会加载global.config.path下面所有的.properties配置文件 
     * 如果没有,就加载classpath下面所有的.properties配置文件 
     * @param autoReload    自动动态加载 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer(boolean autoReload) throws ConfigurationException {
        this(autoReload,null,"");  
    }  
      
    /** 
     * 加载appConfigPath目录下面的所有的.properties配置文件 
     * 加载原理： 
     * 如果加了启动参数:global.config.path,就会加载${global.config.path}/appConfigPath 下面所有的.properties配置文件 
     * 如果没有,就加载${classpath}/appConfigPath下面所有的.properties配置文件 
     * @param autoReload    自动动态加载 
     * @param appConfigPath 应用的配置目录 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer(boolean autoReload, String appConfigPath) throws ConfigurationException {
        this(autoReload,appConfigPath,"");  
    }  
      
      
    /** 
     * 加载appConfigPath目录下面的所有的.properties配置文件 
     * 加载原理： 
     * 如果加了启动参数:global.config.path,就会加载${global.config.path}/appConfigPath 下面所有的.properties配置文件 
     * 如果没有,就加载${classpath}/appConfigPath下面所有的.properties配置文件 
     * @param appConfigPath 应用的配置目录 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer(String appConfigPath) throws ConfigurationException {
        this(true,appConfigPath,"");  
    }  
      
    /** 
     * 加载appConfigPath目录下面指定的configFiles配置文件(多个配置文件用,分隔) 
     * 加载原理： 
     * 如果加了启动参数:global.config.path,就会加载${global.config.path}/appConfigPath下面configFiles里面的配置文件 
     * 如果没有,就加载${classpath}/appConfigPath下面所有的configFiles里面的配置文件 
     * @param autoReload    自动动态加载 
     * @param appConfigPath 应用的配置目录 
     * @param configFiles   多个配置文件用,分隔 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer(boolean autoReload, String appConfigPath, String configFiles) throws ConfigurationException {
        this(true,appConfigPath,StringUtils.split(configFiles, ","));  
    }  
      
    /** 
     * 加载appConfigPath目录下面指定的configFiles配置文件(多个配置文件用,分隔) 
     * 加载原理： 
     * 如果加了启动参数:global.config.path,就会加载${global.config.path}/appConfigPath下面configFiles[]里面的配置文件 
     * 如果没有,就加载${classpath}/appConfigPath下面所有的configFiles[]里面的配置文件 
     * @param appConfigPath 应用的配置目录 
     * @param configFiles   配置文件数组 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer(String appConfigPath, String [] configFiles) throws ConfigurationException {
        this(true,appConfigPath,configFiles);  
    }  
      
      
    /** 
     * 加载appConfigPath目录下面指定的configFiles配置文件(多个配置文件用,分隔) 
     * 加载原理： 
     * 如果加了启动参数:global.config.path,就会加载${global.config.path}/appConfigPath下面configFiles[]里面的配置文件 
     * 如果没有,就加载${classpath}/appConfigPath下面所有的configFiles[]里面的配置文件 
     * @param autoReload    自动动态加载 
     * @param appConfigPath 应用的配置目录 
     * @param configFiles   配置文件数组 
     * @throws ConfigurationException
     */  
    private PropertyPlaceholderConfigurer(boolean autoReload, String appConfigPath, String [] configFiles) throws ConfigurationException {
        this.autoReload = autoReload;  
        this.appConfigPath = appConfigPath;  
        this.configFiles = configFiles;  
        init();/**完成初始化的一些相关操作**/  
        loadConfigFiles();  
    }  
      
      
    /** 
     * 加载配置文件 
     * @throws ConfigurationException
     */  
    private void loadConfigFiles() throws ConfigurationException {
          
        String basePath = BASE_PATH + File.separator + appConfigPath;  
        /**如果没有写配置文件,则加载目录下面所有的.properties文件**/  
        if(ArrayUtils.isEmpty(configFiles)){  
            File  confDirFile = null;  
            if(StringUtils.isBlank(appConfigPath)){  
                confDirFile = new File(BASE_PATH);  
            }else{  
                confDirFile = new File(BASE_PATH, appConfigPath);  
            }  
            if (!confDirFile.exists()){  
                throw new RuntimeException("Config directory <" + confDirFile.getAbsolutePath() + "> doesn't exists.");  
            }  
            /**文件名**/  
            String[] files = confDirFile.list(new FilenameFilter() {  
                @Override  
                public boolean accept(File dir, String name) {  
                    if (!name.endsWith(".properties")) {  
                        return false;  
                    }  
                    if (name.equals("log4j.properties")) {/**过滤掉log4j的配置文件**/  
                        return false;  
                    }  
                    return true;  
                }  
            });  
            for(String file : files){  
                PropertiesConfiguration prop = new PropertiesConfiguration();
                prop.setBasePath(basePath);  
                prop.setFileName(file);  
                prop.setAutoSave(false);  
                if(autoReload){/**重载策略,5秒钟监视文件变化***/  
                    prop.setReloadingStrategy(new FileChangedReloadingStrategy());
                }  
                prop.load();/**加载配置**/  
                propList.add(prop);  
                props.addConfiguration(prop);  
            }  
        }else{  
            for(String file : configFiles){  
                PropertiesConfiguration prop = new PropertiesConfiguration();
                prop.setBasePath(basePath);  
                prop.setFileName(file);  
                prop.setAutoSave(false);  
                if(autoReload){/**重载策略,5秒钟监视文件变化***/  
                    prop.setReloadingStrategy(new FileChangedReloadingStrategy());
                }  
                prop.load();/**加载配置**/  
                propList.add(prop);  
                props.addConfiguration(prop);  
            }  
        }  
    }  
      
      
      
    public String getAppConfigPath() {  
        return appConfigPath;  
    }  
  
    public boolean isAutoReload() {  
        return autoReload;  
    }  
  
    /** 
     * 获取配置项(返回结果为String) 
     * @param key 
     * @return 
     */  
    public String getProperty(String key){  
        return StringUtils.trim(props.getString(key));  
    }  
      
    /** 
     * 获取Boolean类型的配置项 
     * 注:无该配置荐时,返回false 
     * @param key 
     * @return 
     */  
    public Boolean getBooleanProperty(String key){  
        return props.getBoolean(key,false);  
    }  
      
      
    /** 
     * 获取Integer类型的配置项 
     * 注:无该配置荐时,返回new Integer(0) 
     * @param key 
     * @return 
     */  
    public Integer getIntegerProperty(String key){  
        return props.getInteger(key, new Integer(0));  
    }  
      
      
    /** 
     * 获取Integer类型的配置项 
     * 注:无该配置荐时,返回new Float(0) 
     * @param key 
     * @return 
     */  
    public Float getFloatProperty(String key){  
        return props.getFloat(key, new Float(0));  
    }  
      
      
    /** 
     * 获取Long类型的配置项 
     * 注:无该配置荐时,返回new Long(0) 
     * @param key 
     * @return 
     */  
    public Long getLongProperty(String key){  
        return props.getLong(key, new Long(0));  
    }  
      
      
    /** 
     * 获取Double类型的配置项 
     * 注:无该配置荐时,返回new Double(0) 
     * @param key 
     * @return 
     */  
    public Double getDoubleProperty(String key){  
        return props.getDouble(key, new Double(0));  
    }  
      
      
    /** 
     * 获取Short类型的配置项 
     * 注:无该配置荐时,返回new Short(0) 
     * @param key 
     * @return 
     */  
    public Short getShortProperty(String key){  
        return props.getShort(key, Short.valueOf("0"));  
    }  
      
      
    /** 
     * 获取String []类型的配置项 
     * 注:配置项各字符之前通过,分隔 eg param =a,b,c,d 
     * @param key 
     * @return 
     */  
    public String [] getStringArrayProperty(String key){  
        return  props.getStringArray(key);  
    }  
      
      
      
      
    /** 
     * 获取BigDecimal类型的配置项 
     * 注:无该配置荐时,返回new BigDecimal("0") 
     * @param key 
     * @return 
     */  
    public BigDecimal getBigDecimalProperty(String key){  
        return props.getBigDecimal(key, new BigDecimal("0"));  
    }  
      
      
    /** 
     * 重新加载配置项 
     */  
    public void reloadProperties(){  
        for(PropertiesConfiguration prop : propList){
            prop.reload();  
        }  
    }  
      
    /** 
     * 刷新配置项 
     * @throws ConfigurationException
     */  
    public void refreshProperties() throws ConfigurationException {
        for(PropertiesConfiguration prop : propList){
            prop.refresh();  
        }  
    }  
      
      
      
    /** 
     * 返回构建 PropertyPlaceholderConfigurer(动态加载配置文件)的Builder 
     * @return 
     */  
    public static PropertyPlaceholderBuilder getBuilder(){  
        return new PropertyPlaceholderBuilder();  
    }  
      
    /** 
     * 构建 PropertyPlaceholderConfigurer(动态加载配置文件) 
     * PropertyPlaceholderConfigurer这个类参数比较多,固使用Builder模式 
     * @author zhangjiawei 
     * 
     */  
    public static class PropertyPlaceholderBuilder{  
          
        /**应用程序在配置分离下面的子目录**/  
        private String appConfigPath = "";  
          
        /**要加载的配置文件,多个配置文件用,分隔,不写则加载目录下面的所有.properties文件**/  
        private String [] configFiles;  
          
        /**自动动态加载,默认为true**/  
        private boolean autoReload = true;  
          
        /** 
         * 设置应用的配置目录 
         * @param appConfigPath 应用的配置目录 
         */  
        public PropertyPlaceholderBuilder appConfigPath(String appConfigPath){  
            this.appConfigPath = appConfigPath;  
            return this;  
        }  
          
        /** 
         * 设置是否自动动态加载 
         * @param autoReload 自动动态加载 
         */  
        public PropertyPlaceholderBuilder autoReload(boolean autoReload){  
            this.autoReload = autoReload;  
            return this;  
        }  
          
        /** 
         * 设置配置文件,多个配置文件用,分隔 
         * @param configFiles 
         */  
        public PropertyPlaceholderBuilder configFiles(String configFiles){  
            this.configFiles = addConfigFiles(StringUtils.split(configFiles, ","));  
            return this;  
        }  
          
        /** 
         * 设置配置文件数组 
         * @param configFiles 
         */  
        public PropertyPlaceholderBuilder configFiles(String [] configFiles){  
            this.configFiles = addConfigFiles(configFiles);  
            return this;  
        }  
          
        /** 
         * 添加配置文件 
         * @param configFiles 
         * @return 
         */  
        private String [] addConfigFiles(String [] configFiles){  
            if(this.configFiles != null && configFiles != null && configFiles.length>0){  
                return (String[]) ArrayUtils.addAll(this.configFiles, configFiles);  
            }else{  
                return this.configFiles;  
            }  
        }  
          
        /** 
         * 生成PropertyPlaceholderConfigurer对象 
         * @return 
         * @throws ConfigurationException
         */  
        public PropertyPlaceholderConfigurer build() throws ConfigurationException {
            return  new PropertyPlaceholderConfigurer(autoReload, appConfigPath,configFiles);  
        }  
    }  
      
      
    /******************  main test ****************************/  
      
    public static void main(String[] args) {  
        try {  
            PropertyPlaceholderConfigurer conf = new PropertyPlaceholderConfigurer();  
            System.out.println(PropertyPlaceholderConfigurer.BASE_PATH);  
              
              
            conf = PropertyPlaceholderConfigurer.getBuilder().autoReload(true).appConfigPath("openapi-service").configFiles("mail.properties,common.properties").configFiles(new String [] {"sysConfig.properties"}).build();  
              
            /** 
             *  
             * -Dglobal.config.path="D:\eclipse\workspace\openapi-service\env\test" 
             * 
             **/  
//          conf = new PropertyPlaceholderConfigurer(true,"openapi-service");  
//            
//          conf.getAppConfigPath();  
//            
//          conf = new PropertyPlaceholderConfigurer(true,"openapi-service",new String []{"cod_support_rule.properties","common.properties"});  
//            
//          conf.getAppConfigPath();  
              
            while(true){  
                System.out.println(conf.getProperty("mail.reconciliation.smtp.auth"));  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
              
              
        } catch (ConfigurationException e) {
            e.printStackTrace();  
        }  
          
    }  
      
      
      
} 