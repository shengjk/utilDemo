package xmht.java8;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * @author shengjk1
 * @date 2019/11/27
 */
public class JavaTimeDamo {
	public static void main(String[] args) {
		Instant now = Instant.now();
		System.out.println("=========asdqewerewrerrewrrewraaaaa");
		Instant end = Instant.now();
		//两个瞬点的时差
		long l = Duration.between(now, end).toNanos();
		
		
		LocalDate now1 = LocalDate.now();
		LocalDate of = LocalDate.of(1903, 6, 14);
		System.out.println(now1);
		System.out.println(LocalDate.of(1913, 6, 14));
		System.out.println(LocalDate.of(1903, Month.APRIL, 14));
		System.out.println(now1.getDayOfMonth());
		System.out.println(now.isBefore(end));
		
		System.out.println(LocalDate.of(2019, 1, 1).plusDays(255));
		//返回两个本地日期之间的距离
//		System.out.println(now.until(of, ChronoUnit.DAYS));
		
		System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
		//7,而 Calendar 中 周六对应着 7
		System.out.println(DayOfWeek.SUNDAY);
		
		//时间调节器
		LocalDate with = LocalDate.of(2019, 11, 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
		System.out.println(with);
	}
}
