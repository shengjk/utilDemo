
package util.time;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * 返回当天时间
 */
public class AcctDate {
	private static Logger logger=Logger.getLogger(AcctDate.class);
	public static String nowDate(){
//		返回当天时间
//		SimpleDateFormat df =new SimpleDateFormat("yyyyMMdd");
//		String date = df.format(new Date());
//		return date;
		Date dNow = new Date();   //当前时间
		Date dBefore = null;;
		
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
//		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
//		dBefore = calendar.getTime();   //得到前一天的时间
		dBefore = calendar.getTime();   
		
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss"); //设置时间格式
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00.000"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);   
		return defaultStartDate;
	}
	
	 public static void main(String[] args) {
		String yesterday=AcctDate.nowDate();
		System.out.println(yesterday);
	}
	 
	
}
