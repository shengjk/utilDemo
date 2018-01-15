package util.kafka.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class kafkaProducerFromFile {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "TestPartition");
        Producer producer = new KafkaProducer<String, String>(props);

        StringBuffer sb= new StringBuffer("");
        FileReader reader = new FileReader("data/insert.txt");

        BufferedReader br = new BufferedReader(reader);
        String str = null;
        int i=0;
        while((str = br.readLine()) != null) {
//            sb.append(str+"/n");
            if (i==50){
                break;
            }
            if (str.contains("\"orders\"")){
                Future<RecordMetadata> recordMetadataFuture = producer.send(new ProducerRecord<String, String>("test03", "3", str));
                recordMetadataFuture.get();
                i++;
            }else {
//                sendMsg(props, str,"infos");
            }
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        br.close();
        reader.close();

        //生产者发送消息
        String topic = "topic01";
//        Producer<String, String> procuder = new KafkaProducer<String,String>(props);
//        for (int i = 1; i <= 10; i++) {
//            String value = "value_" + i;
//            ProducerRecord<String, String> msg = new ProducerRecord<String, String>(topic, value);
//            procuder.send(msg);
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        //列出topic的相关信息
//        List<PartitionInfo> partitions = new ArrayList<PartitionInfo>() ;
//        partitions = procuder.partitionsFor(topic);
//        for(PartitionInfo p:partitions)
//        {
//            System.out.println(p);
//        }
//
//        System.out.println("send message over.");
////        procuder.close(100,TimeUnit.MILLISECONDS);
//        procuder.close();
    }

}
