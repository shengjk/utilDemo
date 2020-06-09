package xmht.java8;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * @author shengjk1
 * @date 2019/11/22
 */
public class Java8Demo {
	public static void main(String[] args) {
		ArrayList<String> strings = new ArrayList<>();
		strings.add("a1");
		strings.add("a");
		strings.add("a2");
		
//		strings.forEach(x-> System.out.println(x));
		/*
		方法引用：想要传递给其他代码的操作已经有实现的方法了
		对象::实例方法
		类::静态方法
		类::实例方法
		 */
		strings.forEach(System.out::println);
		String[] strings1 = strings.toArray(new String[strings.size()]);
		Stream.of(strings1).forEach(System.out::println);
		System.out.println("=========");
		Arrays.sort(strings1,String::compareToIgnoreCase);
		Stream.of(strings1).forEach(System.out::println);
//		Math::pow
//		String :: compareToIgnoreCase
//		this::equals
//		super::实例方法
		
		Comparator<String> stringComparator =
				(String first, String second) -> Integer.compare(first.length(), second.length());
		
		BiFunction<String,String,Integer> comp=(String first, String second) -> Integer.compare(first.length(), second.length());
		
		Runnable sleeper=()->{
			System.out.println("Zzzz");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		};
		sleeper.run();
		Greeter greeter = new Greeter();
//		int[]::new等同于lambda x->new int[x]
		
	}
	
	
	/*
	lambda表达式有两个自由变量(那些不是参数并且没有在代码中定义的变量) text和 count。
	含有自由变量的代码块称为 闭包
	lambda表达式中，被引用的变量的值不可以被更改
	因为更改 lambda表达式中的变量不是线程安全的
	不要指望编译器会捕获所有并发访问错误。不可变的约束只作用在局部变量上。如果 matches 是一个实例变量或者某个
	闭合类的静态变量，那么不会有任何错误被报告出来
	 */
	public static void repeatMessage(String text, int count){
		Runnable runnable = () -> {
			for (int i = 0; i < count; i++) {
				System.out.println(text);
				Thread.yield();
			}
		};
		new Thread(runnable).start();
	}
	
	/*
	this,会引用创建该lambda表达式的方法的this参数(创建lambda表达式的方法所在类的this引用)
	lambda表达式中使用this与在其他地方使用this没有什么区别
	 */
	public void doWork(){
		Runnable runner=()->{
			System.out.println(this.toString());
		};
	}
}



class Greeter{
	public void greet(){
		System.out.println("Hello world!");
	}
}

class ConcurrentGreeter extends Greeter{
	@Override
	public void greet(){
		Thread thread=new Thread(super::greet);
		thread.start();
	}
}
