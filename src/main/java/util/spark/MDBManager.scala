package util.spark

import java.io.{File, FileInputStream, InputStream}
import java.sql.Connection
import java.util.Properties

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.apache.spark.SparkFiles

/**
  * Created by shengjk1 on 2018/1/27
  */
class MDBManager (isLocal:Boolean) extends Serializable{
	private val cpds: ComboPooledDataSource = new ComboPooledDataSource(true);
	private val prop = new Properties()
	private var in:InputStream = _
	isLocal match{
		case true  => in = getClass().getResourceAsStream("/conf.properties");
		case false => in = new FileInputStream(new File(SparkFiles.get("conf.properties")))
	}
	try {
		prop.load(in);
		cpds.setJdbcUrl(prop.getProperty("c3p0_jdbcUrl").toString());
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setUser(prop.getProperty("c3p0_user").toString());
		cpds.setPassword(prop.getProperty("c3p0_password").toString());
		cpds.setMaxPoolSize(Integer.valueOf(prop.getProperty("c3p0_maxPoolSize").toString()));
		cpds.setMinPoolSize(Integer.valueOf(prop.getProperty("c3p0_minPoolSize").toString()));
		cpds.setAcquireIncrement(Integer.valueOf(prop.getProperty("c3p0_acquireIncrement").toString()));
		cpds.setInitialPoolSize(Integer.valueOf(prop.getProperty("c3p0_initialPoolSize").toString()));
		cpds.setMaxIdleTime(Integer.valueOf(prop.getProperty("c3p0_maxIdleTime").toString()));
		cpds.setPreferredTestQuery("select 1")
		cpds.setIdleConnectionTestPeriod(60)
		cpds.setTestConnectionOnCheckin(true)
	} catch {
		case ex: Exception => ex.printStackTrace()
	}
	def getConnection:Connection={
		try {
			return cpds.getConnection();
		} catch {
			case ex:Exception => ex.printStackTrace()
				null
		}
	}
}
object MDBManager{
	var mdbManager:MDBManager=_
	def getMDBManager(isLocal:Boolean):MDBManager={
		synchronized{
			if(mdbManager==null){
				mdbManager = new MDBManager(isLocal)
			}
		}
		mdbManager
	}
}