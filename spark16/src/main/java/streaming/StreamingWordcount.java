package streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

public class StreamingWordcount {

	// yum install nc
	// nc -lk 8888
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("StreamingWordcount").setMaster("local[2]");
		/*
		
		 * 创建该对象类似于spark core中的JavaSparkContext,类似于SparkSQL中SQLContext
		 * 该对象除了接受SparkConf对象,还接收一个BatchInterval参数,就是说,每收集多长时间的数据划分为一个Batch即RDD去执行
		 * 这里Durations可以设置分钟、毫秒、秒
		 */
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(10)) ;
		
		/*
		 * 首先创建输入DStream,代表一个数据源比如这里从socket或Kafka来持续不断的进入实时数据流
		 * 创建一个监听socket数据量,RDD里面的每一个元素就是一行行的文本
		 */
		JavaReceiverInputDStream<String> lines = jssc.socketTextStream("centos4", 8888);
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(" "));
			}
		});
		JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word, 1);
			}
		});
		JavaPairDStream<String, Integer> wordcounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});
		
		// 最后每次计算完成,都打印一下这10秒钟的单词统计情况
		wordcounts.print();
		
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
}
