package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

// reduceByKey = groupByKey + reduce
// shuffle 洗牌  = map端 + reduce端
// spark里面这个reduceByKey在map端自带Combiner
/*
This will also perform the merging locally on each mapper before sending results to a reducer, similarly
   * to a "combiner" in MapReduce. Output will be hash-partitioned with the existing partitioner/
   * parallelism level.
   
   有可能会发生shuffle也可能不会发生shuffle,当不发生shuffle时，mapPartitions Iterator进行map端的数据合并
 */
public class ReduceByKeyOperator {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("LineCount").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		List<Tuple2<String,Integer>> scoreList = Arrays.asList(
				new Tuple2<String, Integer>("xuruyun" , 150),
				new Tuple2<String, Integer>("liangyongqi" , 100),
				new Tuple2<String, Integer>("wangfei" , 100),
				new Tuple2<String, Integer>("wangfei" , 80));
		
		JavaPairRDD<String, Integer> rdd = sc.parallelizePairs(scoreList);
		
		rdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		}).foreach(new VoidFunction<Tuple2<String,Integer>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Integer> tuple) throws Exception {
				System.out.println("name ： " + tuple._1 + " score :" + tuple._2);
			}
		});
		
		sc.close();
	}
}
