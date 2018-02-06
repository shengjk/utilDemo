package streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by jk on 2016/8/1.
 */
public class SparkStreaming {
	public static void main(String[] args) {
		SparkConf conf=new SparkConf().setAppName("SparkStreaming")
				.setMaster("local[4]");
		JavaStreamingContext ssc=new JavaStreamingContext(conf, Durations.seconds(10));
		
		
//		ssc.stop();
	}
}
