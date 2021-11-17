package com.atguigu.spark.sql

import org.apache.parquet.filter2.predicate.Operators.UserDefined
import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @ClassName ScalaDemo-sql_aggregate_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日20:00 - 周六
 * @Describe 聚合算平均值方式一
 */
object sql_udfAvg_3 {
  def main(args: Array[String]): Unit = {

    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 执行逻辑操作
    val df: DataFrame = spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json")

    df.createOrReplaceTempView("user")

    spark.udf.register("ageAvg", new MyAvgUDAF())
    spark.sql("select ageAvg(age) from user").show


    //todo 关闭环境
    spark.stop()
  }
}

/*
* 自定义聚合函数类：计算年龄的平均值
* 1.继承UserDefinedAggregateFunction
* 2.重写方法
* */

class MyAvgUDAF extends UserDefinedAggregateFunction {
  //表示输入结构：In
  override def inputSchema: StructType = {
    //参数1：列名，参数2，数据类型
    StructType(Array(StructField("age", LongType))) //只会传一个参数进来，所以此参数索引为0
  }

  //缓冲区做计算的数据结构：Buffer
  override def bufferSchema: StructType = {
    //其中total是age的总和，count是有多个个age,也就是多少人
    StructType(Array(
      StructField("total", LongType), //索引是0
      StructField("count", LongType)) //索引是1
    )
  }

  //函数计算结果的数据类型：Out
  override def dataType: DataType = LongType

  //函数的稳定性
  override def deterministic: Boolean = true

  //缓冲区初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    //表示的是total的初始值，0是total的索引位置
    buffer.update(0, 0L) //buffer(0)=0L
    //表示的是count的初始值，1是count的索引位置
    buffer.update(1, 0L) //buffer(1)=0L

  }

  //根据输入的值来更新缓冲区，也就是累加操作
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    //buffer中有两个值，其中total的索引是0，count的索引是1，而传入进来的age只有一个值，所以input索引就只有0
    buffer.update(0, buffer.getLong(0) + input.getLong(0)) //将age累加
    buffer.update(1, buffer.getLong(1) + 1) //将count取出来加1后更新进去,表示人数加1

  }

  //缓冲区合并,因为可能有多个缓冲区,两两聚合，类似于scala中reduce
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    //聚合total
    buffer1.update(0, buffer1.getLong(0) + buffer2.getLong(0))
    //聚合count
    buffer1.update(1, buffer1.getLong(1) + buffer2.getLong(1))
  }

  //计算平均值
  override def evaluate(buffer: Row): Any = {
    //total/count
    buffer.getLong(0) / buffer.getLong(1)
  }
}

























































