package util;

import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class RedisCluster1 extends JedisCluster implements Serializable{
    
    
    private final AtomicReference<RedisClusterNodeStatus> nodeStatus = new AtomicReference<RedisClusterNodeStatus>();
    
    
    public RedisCluster1(HostAndPort nodes, int timeout, int  maxRedirections) {
        super(nodes, 2000 * 10,100);
        nodeStatus.set(new RedisClusterNodeStatus(getClusterNodes()));
    }

    public RedisClusterPipeline pipelined() {
        return new RedisClusterPipeline();
    }

    private class RedisClusterNodeStatus {
        
        private Map<String, JedisPool> nodeMap;
        
        private TreeMap<Long, String> slotHostMap;
        
        public RedisClusterNodeStatus(Map<String, JedisPool> nodeMap) {
            this.nodeMap = nodeMap;
            this.slotHostMap = getSlotHostMap(nodeMap.keySet().iterator().next());
        }
        
        @SuppressWarnings("unchecked")
        private TreeMap<Long, String> getSlotHostMap(String anyHostAndPortStr) {
            TreeMap<Long, String> tree = new TreeMap<Long, String>();
            String parts[] = anyHostAndPortStr.split(":");
            HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
            try {
                Jedis jedis = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
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
