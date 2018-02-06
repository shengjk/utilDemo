package streaming;

import com.google.common.base.Optional;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shengjk1 on 2016/8/15.
 */
public class UpdateStateByKeyWordcount {
	public static void main(String[] args) {
		
		JavaStreamingContextFactory factory = new JavaStreamingContextFactory() {
			@Override
			public JavaStreamingContext create() {
				return createContext();
			}
		};
		
		JavaStreamingContext ssc = JavaStreamingContext.getOrCreate("./ch", factory);
		
		ssc.start();
		ssc.awaitTermination();
		ssc.stop();
		ssc.close();
		
				
	}
	
	public static JavaStreamingContext createContext(){
		
		SparkConf conf =new SparkConf().setAppName("UpdateStateByKeyWordcount")
				.setMaster("local[2]");
		JavaStreamingContext ssc =new JavaStreamingContext(conf, Durations.seconds(2));
		ssc.checkpoint("./ch");
		
		JavaReceiverInputDStream<String>lines= ssc.socketTextStream("localhost",8888);
		JavaDStream<String>line= lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" "));
			}
		});
		JavaPairDStream<String,Integer>linePair= line.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s,1);
			}
		});
		
		JavaPairDStream<String,Integer>lineresut=linePair.updateStateByKey(new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
			@Override
			public Optional<Integer> call(List<Integer> values, Optional<Integer> state) throws Exception {
				
				Integer newValue=0;
				if(state.isPresent()){
					newValue=state.get();
				}
				for (Integer value:values){
					newValue+=value;
				}
				return Optional.of(newValue);
			}
		});
		lineresut.print();
		return ssc;
	}
}
