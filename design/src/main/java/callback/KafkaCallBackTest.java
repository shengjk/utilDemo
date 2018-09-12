package callback;

/**
 * @author shengjk1
 * @date 2018/9/12
 */
public class KafkaCallBackTest {
	public static void main(String[] args){
		KafkaServer kafkaServer = new KafkaServer();
		KafkaClient kafkaClient = new KafkaClient(kafkaServer);
		
		kafkaClient.sendMsg("11111111");
	}
}
