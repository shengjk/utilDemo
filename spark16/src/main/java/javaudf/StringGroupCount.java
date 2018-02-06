package javaudf;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.types.DataTypes.IntegerType;

/**
 * Created by shengjk1 on 2016/8/9.
 */
public class StringGroupCount extends UserDefinedAggregateFunction {
	@Override
	public StructType inputSchema() {
		List<StructField> fle=new ArrayList<>();
		fle.add(DataTypes.createStructField("name",DataTypes.StringType,true));
		
		return DataTypes.createStructType(fle);
	}
	
	@Override
	public StructType bufferSchema() {
		List<StructField> fle=new ArrayList<>();
		fle.add(DataTypes.createStructField("count", IntegerType,true));
		
		return DataTypes.createStructType(fle);
	}
	
	@Override
	public DataType dataType() {
		return IntegerType;
	}
	
	@Override
	public boolean deterministic() {
		return true;
	}
	
	@Override
	public void initialize(MutableAggregationBuffer buffer) {
		buffer.update(0,0);
	}
	
	//中间计算结果
	@Override
	public void update(MutableAggregationBuffer buffer, Row input) {
		buffer.update(0,buffer.getInt(0)+1);
	}
	
	//对中间结果合并
	@Override
	public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
		buffer1.update(0,buffer1.getInt(0)+buffer2.getInt(0));
	}
	
	//最终返回结果
	@Override
	public Object evaluate(Row buffer) {
		return buffer.get(0);
	}
}
