package util.retry;

import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by shengjk1 on 2018/3/7
 */
public class RedisCluster extends JedisCluster implements Serializable{
    
    
    private final AtomicReference<RedisClusterNodeStatus> nodeStatus = new AtomicReference<RedisClusterNodeStatus>();
    
    
    public RedisCluster(Set<HostAndPort> nodes) {
        super(nodes, 2000 * 10,1000);
        //redisCluster的属性以及RedusCluster的对象
        nodeStatus.set(new RedisClusterNodeStatus(getClusterNodes()));
    }

    public RedisClusterPipeline pipelined() {
        return new RedisClusterPipeline();
    }
	
	
	
	public  void closePipAndCluster(RedisCluster.RedisClusterPipeline pipline,RedisCluster redisCluster) throws Exception{
		if (pipline!=null){
			pipline.close();
		}
		if (redisCluster!=null){
			redisCluster.close();
		}
	}
    
    
    
    
    
    private class RedisClusterNodeStatus {
        
        private Map<String, JedisPool> nodeMap;
        
        private TreeMap<Long, String> slotHostMap;
        
        public RedisClusterNodeStatus(Map<String, JedisPool> nodeMap) {
            this.nodeMap = nodeMap;
            String[] strs = nodeMap.keySet().toArray(new String[nodeMap.keySet().size()]);
            Random random=new Random();
            this.slotHostMap = getSlotHostMap(strs[random.nextInt(strs.length)]);
        }
        
        @SuppressWarnings("unchecked")
        //得到了solt与host的映射关系
        private TreeMap<Long, String> getSlotHostMap(String anyHostAndPortStr) {
            
            TreeMap<Long, String> tree = new TreeMap<Long, String>();
            String parts[] = anyHostAndPortStr.split(":");
            HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
            Jedis jedis = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
            try {
                List<Object> clusterSlots = jedis.clusterSlots();
                for (Object clusterSlot : clusterSlots) {
                    List<Object> list = (List<Object>) clusterSlot;
                    List<Object> master = (List<Object>) list.get(2);
                    String hostAndPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                    tree.put((Long) list.get(0), hostAndPort);
                    tree.put((Long) list.get(1), hostAndPort);
                }
                jedis.close();
            } catch (Exception e) {
                throw new RuntimeException("get slot host map failed : " + anyHostAndPortStr, e);
            }finally {
                jedis.close();
            }
            return tree;
        }

        public Map<String, JedisPool> getNodeMap() {
            return nodeMap;
        }

        public TreeMap<Long, String> getSlotHostMap() {
            return slotHostMap;
        }
        
    }

    
    public class RedisClusterPipeline extends PipelineBase implements Closeable {

        private final Map<String, Jedis> slotJedisMap = new LinkedHashMap<>();

        @Override
        protected Client getClient(String key) {
            return getClient(SafeEncoder.encode(key));
        }

        @Override
        protected Client getClient(byte[] key) {
            Integer slot = JedisClusterCRC16.getSlot(key);
            
            Map.Entry<Long, String> entry = nodeStatus.get().getSlotHostMap().lowerEntry(Long.valueOf(slot + 1));
            Jedis jedis = slotJedisMap.get(entry.getValue());
            if (jedis == null) {
                JedisPool jedisPool = nodeStatus.get().getNodeMap().get(entry.getValue());
                jedis = jedisPool.getResource();
                slotJedisMap.put(entry.getValue(), jedis);
            }
            return jedis.getClient();
        }

        public void sync() {
            if (getPipelinedResponseLength() > 0) {
                for (Jedis jedis : slotJedisMap.values()) {
                    for(Object obj : jedis.getClient().getAll()) {
                        generateResponse(obj);
                    }
                }
            }
        }

        @Override
        public void close() throws IOException {
            for (Jedis jedis : slotJedisMap.values()) {
                jedis.close();
            }
            slotJedisMap.clear();
        }
    }
    
}
