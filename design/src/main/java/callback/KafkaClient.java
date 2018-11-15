package callback;

/**
 * @author shengjk1
 * @date 2018/9/12
 */
public class KafkaClient implements CallBack1 {
	private KafkaServer kafkaServer;
	
	public KafkaClient(KafkaServer kafkaServer) {
		this.kafkaServer = kafkaServer;
	}
	
	public void sendMsg(final String msg) {
		//这里用一个线程就是异步，
		new Thread(new Runnable() {
			@Override
			public void run() {
				kafkaServer.saveMessage(KafkaClient.this, msg);
			}
		}).start();
		
		doOthers();
	}
	
	
	public void doOthers(){
		System.out.println("kafka clients do others");
	}
	
	@Override
	public void solve(String result) {
		if (result.equalsIgnoreCase("ack")){
			System.out.println("kafka client 获取到kafkaServer处理成功的消息 执行callback方法");
		}else{
			System.out.println("kafka client 获取到kafkaServer处理失败的消息 执行callback方法");
			System.out.println("kafka client进行失败处理");
		}
		
	}
}
