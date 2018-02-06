package javasssss.accumulator;

import org.apache.spark.AccumulatorParam;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shengjk1 on 2016/12/13.
 */
public class TestAccumulatorSet implements AccumulatorParam<Set<String>> {
	@Override
	public Set<String> addAccumulator(Set<String> t1, Set<String> t2) {
		for (String str:t1) {
			t2.add(str);
		}
		return t2;
	}
	
	@Override
	public Set<String> addInPlace(Set<String> r1, Set<String> r2) {
		for (String str:r2) {
			r1.add(str);
		}
		return r1;
	}
	
	@Override
	public Set<String> zero(Set<String> initialValue) {
		Set<String> set=new HashSet<>();
//		set.add("#");
		return set;
	}
}
