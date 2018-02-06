package javasssss;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Created by 小省  on 2016/6/14.
 */
public class CoalesceOperator {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("CoalesceOperator").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> staffList = Arrays.asList("xuruyun1", "xuruyun2", "xuruyun3"
				, "xuruyun4", "xuruyun5", "xuruyun6"
				, "xuruyun7", "xuruyun8", "xuruyun9"
				, "xuruyun10", "xuruyun11", "xuruyun12");
		JavaRDD<String> staffRDD = sc.parallelize(staffList, 6);

		JavaRDD<String> staffRDDs = staffRDD.mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {
			@Override
			public Iterator<String> call(Integer integer, Iterator<String> stringIterator) throws Exception {
				List<String> list = new ArrayList<String>();
				while (stringIterator.hasNext()) {
					String staff = stringIterator.next();
					list.add("部门[" + (integer + 1) + "] " + staff);
				}
				return list.iterator();
			}
		}, true);
		for (String staffinfo:staffRDDs.collect()){
			System.out.println(staffinfo);
		}

		JavaRDD<String> staffRDD3= staffRDDs.coalesce(3);
		JavaRDD <String> staffRDD4=staffRDD3.mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {
			@Override
			public Iterator<String> call(Integer integer, Iterator<String> stringIterator) throws Exception {
				List<String> list =new ArrayList<String>();
				while (stringIterator.hasNext()){
					String staff =stringIterator.next();
					list.add("部门["+(integer+1)+"]"+staff);
				}
				return list.iterator();
			}
		},true);

		for (String staff:staffRDD4.collect()){
			System.out.println(staff);

		}

		sc.close();


	}
}
