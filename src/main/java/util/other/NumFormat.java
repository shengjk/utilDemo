package util.other;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author shengjk1
 * @date 2018/7/13
 */
public class NumFormat {
	public static void main(String[] args) throws UnsupportedEncodingException {
		int a=10;
		int b=2;
		DecimalFormat df = new DecimalFormat("0.0000");//格式化小数
		String num = df.format((float)a/b);//返回的是String类型
		System.out.println("ddd==="+num);
		
		
		NumberFormat instance = NumberFormat.getInstance(Locale.CHINA);
		
		if (instance instanceof DecimalFormat){
			((DecimalFormat)instance).setDecimalSeparatorAlwaysShown(true);
		}
		
		
		
		String s11="{\"send_speed\":217.3554229736328,\"avg_send_distance\":1000.0,\"courier_id\":\"7079947\",\"pickup_speed\":193.6498565673828,\"id\":\"7079947\",\"order_id\":\"71653027\",\"avg_pickup_distance\":0.0,\"finish_time\":\"20180713 14:34:58.236\"}";
		
		System.out.println(s11.getBytes("utf-8").length);
		
	}
}
