package util;

import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.apache.zookeeper.ZooDefs;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by shengjk1 on 2016/11/4.
 */
public class UpdateOffsert {
	private static Logger logger = Logger.getLogger(UpdateOffsert.class);
	
	public static  void update(final String zkServer,final String zkTopicPath,final AtomicReference<OffsetRange[]> offsetRanges){
		
		ZkClient zkClient = new ZkClient(zkServer);
		OffsetRange[] offsets = offsetRanges.get();
		if (null != offsets) {
			logger.info("scan ===================zk开始更新 offsets" + offsets.length);
			
			for (OffsetRange o : offsets) {
				String zkPath = zkTopicPath + "/" + o.partition();
				//没有会自动创建
//									ZkUtils.updatePersistentPath(zkClient, zkPath, o.untilOffset() + "");
				new ZkUtils(zkClient, null, false).updatePersistentPath(zkPath, o.untilOffset() + "", ZooDefs.Ids.OPEN_ACL_UNSAFE);
				logger.info("scan ===================zk更新完成 path: " + zkPath);
			}
			zkClient.close();
		}
		
	}
	
	
}
