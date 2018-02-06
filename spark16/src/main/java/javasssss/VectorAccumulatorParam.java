package javasssss;

import org.apache.spark.AccumulatorParam;

/**
 * Created by 小省 on 2016/6/28.
 */
public class VectorAccumulatorParam implements AccumulatorParam<String> {
	@Override
	public String addAccumulator(String t1, String t2) {
		t1 =t1+t2;
		return t1;
	}

	@Override
	public String addInPlace(String r1, String r2) {
//		r1 =r1+r2;
		return r1;
	}

	@Override
	public String zero(String initialValue) {
		return "0";
	}
}
