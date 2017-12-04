package util.collection;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by shengjk1 on 2017/7/17
 */
public class BeforeGroupByTop1 extends UDF {
	public String evaluate(String dataTime) {
		String result="";
		if (null==dataTime){
			return null;
		}else {
			String[] strArray = dataTime.replace(" ","").split(",");
			TreeSet<String> set = new TreeSet<>(Arrays.asList(strArray));
//			set.forEach(x-> System.out.println(x));
			
			set = (TreeSet<String>) set.descendingSet();
//			set.forEach(x-> System.out.println(x));
			return set.first();
		}
	}
	
//	public static void main(String[] args) {
//		String a=new BeforeGroupByTop1().evaluate("2016-10-12,  2016-10-12, 2016-10-12, 2016-12-15, 2016-11-02, 2016-11-11, 2016-11-11, 2016-11-11, 2016-11-30, 2016-11-30, 2016-11-30");
//		System.out.println(a);
//
//	}
}
