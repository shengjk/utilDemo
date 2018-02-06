package javasssss.accumulator;

import org.apache.spark.AccumulatorParam;

/**
 * Created by 小省 on 2016/6/28.
 *
 *
 * 执行顺序:
 
 1.调用zero(initialValue)，返回zeroValue
 
 2.调用addAccumulator(zeroValue,1) 返回v1.
 
 调用addAccumulator(v1,2)返回v2.
 
 调用addAccumulator(v2,3)返回v3.
 
 调用addAccumulator(v3,4)返回v4.
 
 3.调用addInPlace(initialValue,v4)
 
 因此最终结果是zeroValue+1+2+3+4+initialValue.
 *
 *
 */
public class VectorAccumulatorParam implements AccumulatorParam<String> {
	
	@Override
	public String addAccumulator(String t1, String t2) {
		t1 =t1+t2;
		return t1;
	}
	
	//执行完addAccumulator方法之后，最后会执行这个方法，将value加到init。
	@Override
	public String addInPlace(String r1, String r2) {
		r1 =r1+r2;
		return r1;
	}
	
	/*
		* init 就是SparkContext.accumulator(init)参数init。
		* 这里的返回值是累计的起始值。注意哦，他可以不等于init。
		*
		* 如果init=10,zero(init)=0,那么运算过程如下:
		* v1:=0+step
		* v1:=v1+step
		* ...
		* ...
		* 最后v1:=v1+init
		**/
	@Override
	public String zero(String initialValue) {
		return "";
	}
}
