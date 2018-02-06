package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

/**
 * Created by 小省 on 2016/6/22.
 */
//二次排序
public class SecondSort {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("SecondSort")
				.setMaster("local");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> lines = sc.textFile("sort.txt");


	JavaPairRDD<SecondSortKey,String> pairs= lines.mapToPair(new PairFunction<String, SecondSortKey, String>() {
		private static final long serialVersionUID = 2610033413733083217L;
		
		@Override
			public Tuple2<SecondSortKey, String> call(String s) throws Exception {
				String[] lin = s.split("\\s+");
				SecondSortKey sk = new SecondSortKey(Integer.valueOf(lin[0]), Integer.valueOf(lin[2]));
				return new Tuple2<SecondSortKey, String>(sk, s);
			}
		});
		JavaPairRDD<SecondSortKey,String> re=pairs.sortByKey();
		re.foreach(new VoidFunction<Tuple2<SecondSortKey, String>>() {
			private static final long serialVersionUID = -7871913217325081528L;
			
			@Override
			public void call(Tuple2<SecondSortKey, String> secondSortKeyStringTuple2) throws Exception {
				System.out.println(secondSortKeyStringTuple2._2);

			}
		});

	sc.close();

	}
}
