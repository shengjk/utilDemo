package other;

/**
 * Created by shengjk1 on 2017/12/4
 */
public class Demo {
	public static void main(String[] args) {
		String a=Thread.currentThread().getContextClassLoader().getResource("").getFile()+ "conf.properties";
		System.out.println(a);
		
	}
}
