package java8.future;

import java.util.concurrent.*;

/**
 * Future java5 添加的类，用来描述一个异步计算的结果。
 * 可以使用 isDone 计算是否完成
 * get 阻塞中线程，直到返回结果
 * cancel 停止任务执行
 *
 *虽然Future以及相关使用方法提供了异步执行任务的能力，但是对于结果的获取却是很不方便，
 * 只能通过阻塞或者轮询的方式得到任务的结果。阻塞的方式显然和我们的异步编程的初衷相违背，
 * 轮询的方式又会耗费无谓的CPU资源，而且也不能及时地得到计算结果，为什么不能用观察者设计模式当计算结果完成及时通知监听者呢？
 *
 * 在Java 8中, 新增加了一个包含50个方法左右的类: CompletableFuture，提供了非常强大的Future的扩展功能，
 * 可以帮助我们简化异步编程的复杂性，提供了函数式编程的能力，可以通过回调的方式处理计算结果，
 * 并且提供了转换和组合CompletableFuture的方法。
 */
public class BasicFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<Integer> f = es.submit(() ->{
                // 长时间的异步计算
                // ……
                // 然后返回结果
                return 100;
            });
//        while(!f.isDone())
//            ;
        f.get();
    
        es.shutdown();
    
    
        //处理异常
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            int i = 1/0;
            return 100;
        });
//future.join();
//        future.get();
        boolean exceptionally = future.isCompletedExceptionally();
        if (exceptionally){
            System.out.println("exceptionally");
        }
//        future.join();
    
        future.exceptionally(exception -> {
            System.out.println("exception = " + exception);
            return 1;
        }).thenApply(i->i*3)
         .thenAccept(System.out::println);
//        System.out.println(future1.get());
    }
}