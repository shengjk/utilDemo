package log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by yangjinfeng02 on 2016/6/4.
 */
public class Counter implements Runnable {

    private Logger logger = LoggerFactory.getLogger(Counter.class);

    private String counterName;

    public Counter(String counterName) {
        this.counterName = counterName;
    }

    public void run() {
        MDC.put("logFileName", counterName);
        logger.info("start counter {}", counterName);
        MDC.remove("logFileName");
    }
}
