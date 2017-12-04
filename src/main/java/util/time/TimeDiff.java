package util.time;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by shengjk1 on 2017/6/13
 */
public class TimeDiff extends UDF {
	public String evaluate(String lastCommisn,String firstCommisn){
		String result="";
		if (null==lastCommisn || null==firstCommisn ||lastCommisn.trim().length()==0 ||firstCommisn.trim().length()==0){
			return null;
		}
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		//时间解析
		DateTime start = DateTime.parse(firstCommisn, format);
		
		DateTime end =  DateTime.parse(lastCommisn,format);
		Period p = new Period(start,end, PeriodType.days());
		System.out.println(p.getDays());
//		System.out.println(p.getMonths());
		int months = p.getDays();
//		System.out.println(months);
		
		/**
		 * 以天為基準來進行計算，以最小的維度為基準
		 */
		
		if(months<=180){
			result="未到期流失";
		}else if (180<months && months<=365){
			result="到期流失";
		}else {
			result="续约";
		}
		return result;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new TimeDiff().evaluate("2015-01-14","2013-12-19"));
//	}
}
