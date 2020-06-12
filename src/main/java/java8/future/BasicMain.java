package java8.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author shengjk1
 * @date 2020/6/11
 */
public class BasicMain {

//	上面的代码中future没有关联任何的Callback、线程池、异步任务等，如果客户端调用future.get就会一致傻等下去
	public static CompletableFuture<Integer> compute(){
		final CompletableFuture<Integer> future = new CompletableFuture<>();
		return future;
	}
	
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		final CompletableFuture<Integer> f = compute();
		
		class Client extends Thread{
			CompletableFuture<Integer> f;
			
			Client(String threadName,CompletableFuture<Integer> f){
				super(threadName);
				this.f=f;
			}
			
			@Override
			public void run() {
				try {
					System.out.println(this.getName()+":"+f.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		
		new Client("client1",f).start();
		new Client("cleint2",f).start();
		System.out.println("waiting");
		f.complete(100);
		System.out.println(f.get());
		
//		为一段异步执行的代码创建CompletableFuture对象
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			//长时间计算任务
			
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return ".00";
		});
		//complete 不会阻塞
//		boolean complete = future.complete("1");
//		System.out.println(future.get());
		
		/*
		计算结果完成时的处理
		 */
		
		// 当CompletableFuture的计算结果完成，或者抛出异常的时候
		//返回的值跟原始的 CompletableFuture 的计算值相同
		CompletableFuture<String> future1 = future.whenComplete((v, e) -> {
			System.out.println("v=" + v);
			System.out.println("e=" + e);
		});
//		try {
//			TimeUnit.SECONDS.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		System.out.println(future1.get());
		
		
		/*
		转换
		 */
		
		//当原来的CompletableFuture计算完后，将结果传递给函数fn，将fn的结果作为新的CompletableFuture计算结果。
		// 因此它的功能相当于将CompletableFuture<T>转换成CompletableFuture<U>。
		//需要注意的是，这些转换并不是马上执行的，也不会阻塞，而是在前一个stage完成后继续执行。
		//它们与handle方法的区别在于handle方法会处理正常计算值和异常，因此它可以屏蔽异常，避免异常继续抛出。
		// 而thenApply方法只是用来处理正常值，因此一旦有异常就会抛出。
		CompletableFuture<String> future2 = future.thenApplyAsync(i -> i + "aa").thenApply(i -> i.toString());
		System.out.println(future2.get());
		System.out.println("====");
		
		
		/*
		纯消费
		 */
		
		//只对结果执行Action,而不返回新的计算值, 因此计算值为Void
		CompletableFuture<Void> future3 = future.thenAccept(System.out::println);
		System.out.println("void== "+future3.get());
		
		
		//thenAcceptBoth以及相关方法提供了类似的功能，当两个CompletionStage都正常完成计算的时候，就会执行提供的action，它用来组合另外一个异步的结果。
		CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> {
			return 100;
		});
		CompletableFuture<Void> f4 =  future4.thenAcceptBoth(CompletableFuture.completedFuture(10), (x, y) -> System.out.println(x * y));
		System.out.println(f4.get());
		//runAfterBoth是当两个CompletionStage都正常完成计算的时候,执行一个Runnable，这个Runnable并不使用计算的结果。
		
		CompletableFuture<Integer> future5 = CompletableFuture.supplyAsync(() -> {
			return 100;
		});
		CompletableFuture<Void> f5 =  future5.thenRun(() -> System.out.println("finished"));
		System.out.println(f5.get());
		
		
//		因此，你可以根据方法的参数的类型来加速你的记忆。
//		Runnable类型的参数会忽略计算的结果，
//		Consumer是纯消费计算结果，
//		BiConsumer会组合另外一个CompletionStage纯消费，
//		Function会对计算结果做转换，
//		BiFunction会组合另外一个CompletionStage的计算结果做转换。
		
//		......
//		https://colobu.com/2016/02/29/Java-CompletableFuture/
	}
}
