package util.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

/**
 * Created by shengjk1 on 2016/12/5.
 */
public class TimerDemo {
	public static void main(String[] args) throws InterruptedException {
		System.out.println(Instant.now().toEpochMilli());
		System.out.println(Instant.now());
		Instant instant = Instant.now();
		System.out.println("========= " + instant.toEpochMilli());
//		Thread.currentThread().sleep(1000);
		Instant instant1 = instant.now();
		System.out.println("********** " + instant1.toEpochMilli());
		Duration duration = Duration.between(instant, instant1);
		long millis = duration.toMillis();
		System.out.println(millis);
		
		//本地时间
		LocalDate today = LocalDate.now();
		LocalDateTime today1 = LocalDateTime.now();
		LocalTime today2 = LocalTime.now();
		System.out.println(today.toString());
		System.out.println(today1.toString());
		System.out.println(today2.toString());
		
		LocalDate localDate = LocalDate.of(2016, 12, 5);
		System.out.println(localDate.toString());
		
		System.out.println(LocalDate.of(2016, 1, 1).plusDays(256).toString());
		System.out.println(LocalDate.of(2016, 1, 1).plusDays(256).plus(Period.ofYears(1)).toString());
		
		
		LocalDate firstTuesday = LocalDate.of(2016, 12, 1).withDayOfMonth(2);//2016-12-02
		System.out.println(firstTuesday.toString());
		//某一个月的第一个周二
		LocalDate firstTuesday1 = LocalDate.of(2016, 12, 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));//2016-12-02
		System.out.println(firstTuesday1.toString());
		
		//格式化
//		String formatted= DateTimeFormatter.ISO_DATE.format(firstTuesday1);
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
		String formatted = formatter.format(firstTuesday1);
		System.out.println(formatted);
		
		
		System.out.println("=======================================");
		int year=2009;
		while (true){
			yearAndMonthAndseason(year);
			year++;
			if(year==9999){
				break;
			}
			System.out.println("year:  "+year);
		}
	
}
	public static  void yearAndMonthAndseason(int year) {
		
		
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		c.set(year, 0, 1);
		while (true) {
			if (c.get(Calendar.YEAR) != year) {
				break;
			} else {
				String time = df.format(c.getTime());
				
				int week = c.get(Calendar.WEEK_OF_YEAR);
				
				if (c.get(Calendar.MONTH) == 11 && week == 1) {
					week = 53;
				}
				String yearAndMoth = time.substring(0, 7);
				String Moth = time.substring(5, 7);
				System.out.println("moth============ "+Moth);
				int season = 0;
				int month = c.get(Calendar.MONTH);
				switch (month) {
					case Calendar.JANUARY:
					case Calendar.FEBRUARY:
					case Calendar.MARCH:
						season = 1;
						break;
					case Calendar.APRIL:
					case Calendar.MAY:
					case Calendar.JUNE:
						season = 2;
						break;
					case Calendar.JULY:
					case Calendar.AUGUST:
					case Calendar.SEPTEMBER:
						season = 3;
						break;
					case Calendar.OCTOBER:
					case Calendar.NOVEMBER:
					case Calendar.DECEMBER:
						season = 4;
						break;
					default:
						break;
				}
				
				String halfOfYear = "上半年";
				if (month > 6) {
					halfOfYear = "下半年";
				}
				
				
				String result=time + "|" + week + "|" + yearAndMoth + "|" + season + "|" + halfOfYear + "|" + year+"\n";
//				outPut("E:\\工作\\一诺\\一诺数据平台资料（0518）\\",result);
				c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
			}
		}
	}
	
}
