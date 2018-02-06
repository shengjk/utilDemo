package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 小省 on 2016/6/21.
 */
public class GroupTopN {
	public static void main(String[] args) {
		SparkConf conf =new SparkConf().setAppName("GroupTopN").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);

		JavaRDD<String> lines= sc.textFile("score.txt");
		
		JavaPairRDD<String,Integer>pairs= lines.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				String[] ss=s.split(" ");

				return new Tuple2<String, Integer>(ss[0],Integer.valueOf(ss[1]));
			}
		});



		JavaPairRDD<String,Iterable<Integer>> groupPairs=pairs.groupByKey();
		JavaPairRDD<String,Iterable<Integer>> re=groupPairs.mapToPair(new PairFunction<Tuple2<String,Iterable<Integer>>, String, Iterable<Integer>>() {
			@Override
			public Tuple2<String, Iterable<Integer>> call(Tuple2<String, Iterable<Integer>> stringIterableTuple2) throws Exception {
				List<Integer> list =new ArrayList<Integer>();
				Iterator<Integer> iterator=stringIterableTuple2._2.iterator();
				while(iterator.hasNext()){
					list.add(iterator.next());
				}
				java.util.Collections.sort(list);
				return new Tuple2<String, Iterable<Integer>>(stringIterableTuple2._1,list);
			}
		});

		re.foreach(new VoidFunction<Tuple2<String, Iterable<Integer>>>() {
			@Override
			public void call(Tuple2<String, Iterable<Integer>> stringIterableTuple2) throws Exception {
				System.out.println(stringIterableTuple2._1);
				Iterator<Integer> re=stringIterableTuple2._2.iterator();
				while(re.hasNext()){
					System.out.println(re.next());
				}

			}
		});
		
	}
}
