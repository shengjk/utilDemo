package javasssss.accumulator;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.*;

/**
 * Created by 小省 on 2016/7/11.
 */
/*
 spark 累加器 accumulator
 
For accumulator updates performed inside actions only,
Spark guarantees that each task’s update to the accumulator will only be applied once,
i.e. restarted tasks will not update the value. In transformations,
users should be aware of that each task’s update may be applied more than once if tasks or job stages are re-executed
 
 */
public class AccumulatorValue {
	public static void main(String[] args) {
		Test2();
//		SparkConf conf =new SparkConf().setAppName("AccumulatorValue").setMaster("local");
//		JavaSparkContext sc =new JavaSparkContext(conf);
//
//		final  Accumulator<Integer> sum=sc.accumulator(0,"Our Accumulator");
//
//		List<Integer> list= Arrays.asList(1,2,3,4,5,6);
//
//		JavaRDD<Integer> listRdd=sc.parallelize(list);
//
//		listRdd.foreach(new VoidFunction<Integer>() {
//			@Override
//			public void call(Integer integer) throws Exception {
//				//计数器去操作其他的数
//				sum.add(integer);
//				//可以直接进行打印，但通过sum.value方法就会报错  Can't read accumulator value in task
//				System.out.println(sum);
//
//			}
//		});
//
//		//凡是不在RDD中的代码都属于客户端driver
//		System.out.println(sum.value());
//		sc.close();
	}
	
	public static void Test(){
		SparkConf conf =new SparkConf().setAppName("AccumulatorValue").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);
		
		
		final  Accumulator<String> sum=sc.accumulator("",new VectorAccumulatorParam());
		
		List<String> list= Arrays.asList("a","b","v","d","f","g");
		
		JavaRDD<String> listRdd=sc.parallelize(list);
		
		listRdd.foreach(new VoidFunction<String>() {
			@Override
			public void call(String String) throws Exception {
				//计数器去操作其他的数
				sum.add(String);
				//可以直接进行打印，但通过sum.value方法就会报错  Can't read accumulator value in task
				System.out.println(sum);
				
			}
		});
		
		//凡是不在RDD中的代码都属于客户端driver
		System.out.println("++++++++++++++++++++");
		System.out.println(sum.value());
		sc.close();
	}
	
	
	public static void Test1(){
		SparkConf conf =new SparkConf().setAppName("AccumulatorValue").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);
		List<String> list=new ArrayList<>();
		final  Accumulator<List<String>> sum=sc.accumulator(list,"list",new TestAccumulatorList());
		
		List<String> list1= Arrays.asList("a");
		
		JavaRDD<String> listRdd=sc.parallelize(list1);
		
		listRdd.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				list.add(s);
				sum.add(list);
			}
		});
		
		
		listRdd.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				list.add(s);
				sum.add(list);
			}
		});
		
		
		//凡是不在RDD中的代码都属于客户端driver
		System.out.println("++++++++++++++++++++");
		System.out.println(sum.value());
		
		for (String str:sum.value()) {
			System.out.println(str);
		}
		sc.close();
	}
	
	
	
	public static void Test2(){
		SparkConf conf =new SparkConf().setAppName("AccumulatorValue").setMaster("local");
		JavaSparkContext sc =new JavaSparkContext(conf);
		Set<String> set=new HashSet<>();
		
		
		final  Accumulator<Set<String>> sum=sc.accumulator(set,"list",new TestAccumulatorSet());
		
		List<String> list1= Arrays.asList("a","b","c","d");
		
		JavaRDD<String> listRdd=sc.parallelize(list1);
		
		listRdd.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				Set<String> a=new HashSet<String>();
				
				
				for (String str:sum.localValue()) {
					System.out.println("========== "+str);
				}
				a.add(s);
				sum.add(a);
			}
		});
		
		
		listRdd.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				Set<String> a=new HashSet<String>();
				a.add(s);
				sum.add(a);
				
				
			}
		});
		
		
		System.out.println("++++++++++++++++++++");
		System.out.println(sum.value());
		System.out.println(sum.value().size());
		
		for (String str:sum.value()) {
			System.out.println(str);
		}
		
		sum.value().clear();
		//凡是不在RDD中的代码都属于客户端driver
		System.out.println("++++++++++++++++++++");
		System.out.println(sum.value());
		System.out.println(sum.value().size());
		
		for (String str:sum.value()) {
			System.out.println(str);
		}
		sc.close();
	}
	
	
}
