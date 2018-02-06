package javasssss.accumulator;

import org.apache.spark.AccumulatorParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengjk1 on 2016/12/13.
 *
 * List不去重
 */
public class TestAccumulatorList implements AccumulatorParam<List<String>> {
		@Override
		public List<String> addAccumulator(List<String> t1, List<String> t2) {
			for (String str:t2) {
				t1.add(str);
			}
			return  t1;
		}
		
		@Override
		public List<String> addInPlace(List<String> r1, List<String> r2) {
			for (String str:r2) {
				r1.add(str);
			}
			return  r1;
		}
		
		@Override
		public List<String> zero(List<String> initialValue) {
			List<String> list=new ArrayList<>();
			return list;
		}
}
