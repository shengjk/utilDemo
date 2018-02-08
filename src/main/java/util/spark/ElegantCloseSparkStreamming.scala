package util.spark

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.spark_project.jetty.server.handler.{AbstractHandler, ContextHandler}
import org.spark_project.jetty.server.{Request, Server}
import org.slf4j.{Logger, LoggerFactory}
import redis.clients.jedis.{HostAndPort, JedisCluster}
import util.config.ConfigManager

/*** 负责接受http请求来优雅的关闭流
  * @param ssc  Stream上下文
  */
class CloseStreamHandler(ssc:StreamingContext) extends AbstractHandler {
    @transient
    private val logger: Logger = LoggerFactory.getLogger("com.ishansong.util.ElegantCloseSparkStreamming")
    override def handle(s: String, baseRequest: Request, req: HttpServletRequest, response: HttpServletResponse): Unit ={
        logger.info("开始关闭......")
        ssc.stop(true,true)//优雅的关闭
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        val out = response.getWriter();
        out.println("close success");
        baseRequest.setHandled(true);
        logger.info("关闭成功.....")
    }
}
object ElegantCloseSparkStreamming {
    @transient
    private val logger: Logger = LoggerFactory.getLogger("com.ishansong.util.ElegantCloseSparkStreamming")
    /****
      * 负责启动守护的jetty服务
      * @param port 对外暴露的端口号
      * @param ssc Stream上下文
      */
    def daemonHttpServer(port:Int,ssc: StreamingContext)={
        val server=new Server(port)
        val context = new ContextHandler()
        context.setContextPath( "/close" )
        context.setHandler( new CloseStreamHandler(ssc) )
        server.setHandler(context)
        server.start()
    }

    /***
      * 通过一个消息文件来定时触发是否需要关闭流程序
      * @param ssc StreamingContext
      */
    def stopByMarkFile(ssc:StreamingContext):Unit= {
        val intervalMills = 10 * 1000 // 每隔10秒扫描一次消息是否存在
        var isStop = false
        val config =ConfigManager.build()
        val stop_hdfs_file_path =config.getProperty("accumulative_stop_hdfs_file_path") //判断消息文件是否存在，如果存在就
        val conf: SparkConf = ssc.sparkContext.getConf

        while (!isStop) {
            isStop = ssc.awaitTerminationOrTimeout(intervalMills)
            if (!isStop && isExistsMarkFile(stop_hdfs_file_path)) {
                deleteMarkFile(stop_hdfs_file_path)
                logger.info("1秒后开始关闭sparstreaming程序.....")
                Thread.sleep(1000)
                ssc.stop(true, true)
            }
        }
    }

    /***
      * 判断是否存在mark file
      * @param hdfs_file_path  mark文件的路径
      * @return
      */
    def isExistsMarkFile(hdfs_file_path: String):Boolean = {
        val conf = new Configuration()
        val path=new Path(hdfs_file_path)
        val fs =path.getFileSystem(conf)
        fs.exists(path)
    }

    def deleteMarkFile(hdfs_file_path: String) = {
        val conf = new Configuration()
        val path=new Path(hdfs_file_path)
        val fs =path.getFileSystem(conf)
        if(fs.exists(path)) {
           fs.delete(path,true)
        }
    }
    
    
    /*
    ssc.start()
		ElegantCloseSparkStreamming.stopByMarkFile(ssc)
		ssc.awaitTermination()
     */
}
