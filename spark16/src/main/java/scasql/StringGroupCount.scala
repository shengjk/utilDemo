package scasql

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * Created by root on 2016/8/8.
  */
class StringGroupCount extends UserDefinedAggregateFunction{

  // 输入数据的类型
  def inputSchema: StructType = {
    StructType(Array(StructField("str", StringType, true)))
  }

  // 中间结果的类型
  def bufferSchema: StructType = {
    StructType(Array(StructField("count", IntegerType, true)))
  }

  // 还需要分会数据类型
  def dataType : DataType = {
    IntegerType
  }

  def deterministic: Boolean = {
    true
  }

  // 初始值
  def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0
  }

  // 局部累加
  def update(buffer : MutableAggregationBuffer, input : Row):Unit = {
    buffer(0) = buffer.getAs[Int](0) + 1
  }

  // 全局累加
  def merge(buffer:MutableAggregationBuffer, bufferx: Row): Unit ={
    buffer(0) = buffer.getAs[Int](0) + bufferx.getAs[Int](0)
  }

  // 最后有一个方法可以更改你返回的数据样子
  def evaluate(buffer: Row) : Any = {
    buffer.getAs[Int](0)
  }
}
