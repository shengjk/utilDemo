package util.xmht;

/**
 * Created by shengjk1 on 2017/12/4
 */
public class Topic {
	public static void main(String[] args) {
//		ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
//// 创建一个单分区单副本名为t1的topic
//		AdminUtils.createTopic(zkUtils, "t1", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
//		zkUtils.close();
//
		
		/*
		ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
// 删除topic 't1'
		AdminUtils.deleteTopic(zkUtils, "t1");
		zkUtils.close();
		
		
		
		
		//查询topic
		ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
// 获取topic 'test'的topic属性属性
Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
// 查询topic-level属性
Iterator it = props.entrySet().iterator();
while(it.hasNext()){
    Map.Entry entry=(Map.Entry)it.next();
    Object key = entry.getKey();
    Object value = entry.getValue();
    System.out.println(key + " = " + value);
}
zkUtils.close();
		
		
		
		
		//修改topic
		ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
// 增加topic级别属性
props.put("min.cleanable.dirty.ratio", "0.3");
// 删除topic级别属性
props.remove("max.message.bytes");
// 修改topic 'test'的属性
AdminUtils.changeTopicConfig(zkUtils, "test", props);
zkUtils.close();
		
		*/
	}
}
