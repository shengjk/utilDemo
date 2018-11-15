package callback;

/**
 * @author shengjk1
 * @date 2018/9/12
 */
public class KafkaServer {
	
	public void saveMessage(CallBack1 callBack, String msg){
		System.out.println("kafka server access this msg");
		for(int i=0; i<100000;i++){
		}
		
		System.out.println("kafka server save meaage over");
		
		callBack.solve("ack");
		
	}
}
