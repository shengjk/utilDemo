package sudf

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * Created by root on 2016/8/8.
  */
class StringGroupCount extends UserDefinedAggregateFunction{
  override def inputSchema: StructType =
    StructType(Array(StructField("str",StringType,true)))

  //中间结果
  override def bufferSchema: StructType = {
    StructType(Array(StructField("count",IntegerType,true)))
  }

  override def dataType: DataType = IntegerType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit =
    buffer(0)=0

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0)=buffer.getAs[Int](0)+1
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0)=buffer1.getAs[Int](0)+buffer2.getAs[Int](0)
  }

  override def evaluate(buffer: Row): Any = {
    buffer.getAs[Int](0)
  }
}
