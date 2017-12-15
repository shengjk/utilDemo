package log4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangjinfeng02 on 2016/6/4.
 */
public class Application {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; ++i) {
            executorService.execute(new Counter(String.valueOf(i)));
        }
        executorService.shutdown();
    }
}
