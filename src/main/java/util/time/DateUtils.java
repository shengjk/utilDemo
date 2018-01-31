package util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期时间工具类
 *
 *
 import org.apache.commons.lang3.time.FastDateFormat; 线程安全
 */
public class DateUtils {
	
	public static final SimpleDateFormat TIME_FORMAT =
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT =
			new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATEKEY_FORMAT =
			new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 判断一个时间是否在另一个时间之前
	 *
	 * @param time1 第一个时间
	 * @param time2 第二个时间
	 * @return 判断结果
	 */
	public static boolean before(String time1, String time2) {
		try {
			Date dateTime1 = TIME_FORMAT.parse(time1);
			Date dateTime2 = TIME_FORMAT.parse(time2);
			
			if (dateTime1.before(dateTime2)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断一个时间是否在另一个时间之后
	 *
	 * @param time1 第一个时间
	 * @param time2 第二个时间
	 * @return 判断结果
	 */
	public static boolean after(String time1, String time2) {
		try {
			Date dateTime1 = TIME_FORMAT.parse(time1);
			Date dateTime2 = TIME_FORMAT.parse(time2);
			
			if (dateTime1.after(dateTime2)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 计算时间差值（单位为秒）
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return 差值
	 */
	public static int minus(String time1, String time2) {
		try {
			Date datetime1 = TIME_FORMAT.parse(time1);
			Date datetime2 = TIME_FORMAT.parse(time2);
			
			long millisecond = datetime1.getTime() - datetime2.getTime();
			
			return Integer.valueOf(String.valueOf(millisecond / 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 某一个天距今天相差多少天
	 *
	 * @param dat_apply yyyy-mm-dd
	 * @param monthDays one month days
	 * @return
	 */
	public static int DateSubtractFromNow(String dat_apply, int monthDays) {
		LocalDate startFormatted = LocalDate.parse(dat_apply, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate nowFormatted = LocalDate.now();
		Period periodToNextJavaRelease1 = Period.between(startFormatted, nowFormatted);
		return periodToNextJavaRelease1.getMonths() * monthDays + periodToNextJavaRelease1.getDays();
	}
	
	
	/**
	 * 两个日期相差多少天
	 *
	 * @param startDay  yyyy-mm-dd
	 * @param endDay    yyyy-mm-dd
	 * @param monthDays one month days
	 * @return
	 */
	public static int DateSubtractFromSomeDay(String startDay, String endDay, int monthDays) {
		LocalDate startFormatted = LocalDate.parse(startDay, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate endFormatted = LocalDate.parse(endDay, DateTimeFormatter.ISO_LOCAL_DATE);
		Period periodToNextJavaRelease1 = Period.between(startFormatted, endFormatted);
		return periodToNextJavaRelease1.getMonths() * monthDays + periodToNextJavaRelease1.getDays();
	}
	
	
	/**
	 * 获取年月日和小时
	 *
	 * @param datetime 时间（yyyy-MM-dd HH:mm:ss）
	 * @return 结果（yyyy-MM-dd_HH）
	 */
	public static String getDateHour(String datetime) {
		String date = datetime.split(" ")[0];
		String hourMinuteSecond = datetime.split(" ")[1];
		String hour = hourMinuteSecond.split(":")[0];
		return date + "_" + hour;
	}
	
	/**
	 * 获取当天日期（yyyy-MM-dd）
	 *
	 * @return 当天日期
	 */
	public static String getTodayDate() {
		return DATE_FORMAT.format(new Date());
	}
	
	/**
	 * 获取昨天的日期（yyyy-MM-dd）
	 *
	 * @return 昨天的日期
	 */
	public static String getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		
		Date date = cal.getTime();
		
		return DATE_FORMAT.format(date);
	}
	
	/**
	 * 格式化日期（yyyy-MM-dd）
	 *
	 * @param date Date对象
	 * @return 格式化后的日期
	 */
	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}
	
	/**
	 * 格式化时间（yyyy-MM-dd HH:mm:ss）
	 *
	 * @param date Date对象
	 * @return 格式化后的时间
	 */
	public static String formatTime(Date date) {
		return TIME_FORMAT.format(date);
	}
	
	/**
	 * 解析时间字符串
	 *
	 * @param time 时间字符串
	 * @return Date
	 */
	public static Date parseTime(String time) {
		try {
			return TIME_FORMAT.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 格式化日期key
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateKey(Date date) {
		return DATEKEY_FORMAT.format(date);
	}
	
	/**
	 * 格式化日期key
	 *
	 * @param datekey
	 * @return
	 */
	public static Date parseDateKey(String datekey) {
		try {
			return DATEKEY_FORMAT.parse(datekey);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 格式化时间，保留到分钟级别
	 * yyyyMMddHHmm
	 *
	 * @param date
	 * @return
	 */
	public static String formatTimeMinute(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(date);
	}
	
	/**
	 * 获取昨天时间
	 * yyyyMMdd
	 *
	 * @return
	 */
	public static String yesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //设置时间格式
		String formatNowDate = sdf.format(cal.getTime());
		return formatNowDate;
	}
	
	/**
	 * 某天与今天的差，精确到天
	 *
	 * @param da yyyyMMdd
	 * @return
	 * @throws ParseException
	 */
	public static long sevTime(String da) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse(da);
		Date nowDate = new Date();
		long now = nowDate.getTime();
		long date1 = date.getTime();
		
		long result = (now - date1) / (1000 * 60 * 60 * 24);
		return result;
	}
	
	//时间戳转化为时间
	
	/**
	 * @param milliseconds L
	 *
	 *                     Greg   * @return
	 */
	public static String millisecondsToDataStr(long milliseconds) {
		GregorianCalendar gc = new GregorianCalendar();
		Date dat = new Date(milliseconds);
		gc.setTime(dat);
		//24小时
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		//12小时
//		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String sb = format.format(gc.getTime());
		return sb;
	}
	
	
	/**
	 * 时间转化为时间戳
	 *
	 * @param dateTime yyyy-mm-dd
	 * @return
	 */
	public static long DataStrTomilliseconds(String dateTime) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(dateTime).getTime();
	}
	
	public static void main(String[] args) {
		System.out.println(millisecondsToDataStr(1509518423110L));
		try {
			System.out.println(DataStrTomilliseconds("2018-01-02 00:00:00:000"));
			/**1509518424110  1min相差1000
			 * 1509518429110
			 * 1509518423000
			 */
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
