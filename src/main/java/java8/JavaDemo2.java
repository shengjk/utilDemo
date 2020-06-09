package xmht.java8;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author shengjk1
 * @date 2019/11/27
 */
public class JavaDemo2 {
	public static void main(String[] args) {
		repeat(10, i -> System.out.println("CountDown:" + (9 - i)));
		
		
		
	}
	
	public static void doInOrderAsync(Runnable first, Runnable second, Consumer<Throwable> handler){
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					first.run();
					second.run();
				} catch (Throwable e) {
					handler.accept(e);
				}
			}
		};
		thread.start();
	}
	
	public static void repeat(int n, IntConsumer action) {
		for (int i = 0; i < n; i++) {
			action.accept(i);
		}
	}
	
	public static void info(Logger logger, Supplier<String> message) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info(message.get());
		}
	}
	
}
