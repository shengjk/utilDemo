package util.kafka.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;

import java.util.Properties;

/**
 * Created by shengjk1.
 *  生产者可以保证权限认证
 */
public class ProducerZbdba {
	public static void main(String[] args) {
         Properties producerProps = new Properties();
         producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos12:9093");
//         producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "myApiKey");
        producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "F:\\server.keystore.jks");
        producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "123456");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "F:\\server.truststore.jks");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "123456");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
  
         producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
         producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
  
         KafkaProducer producer = new KafkaProducer(producerProps);
         for(int i = 0; i < 100; i++)  
             producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
             System.out.println("test");  
         producer.close();  
    }  
}  