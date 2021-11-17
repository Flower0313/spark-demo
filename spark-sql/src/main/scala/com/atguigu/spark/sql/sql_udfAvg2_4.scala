package com.atguigu.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, Row, SparkSession, functions}
import org.apache.spark.sql.expressions.Aggregator

/**
 * @ClassName ScalaDemo-sql_aggregate_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日20:00 - 周六
 * @Describe 聚合算平均值的方式二
 */
object sql_aggregate_3 {
  def main(args: Array[String]): Unit = {

    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 执行逻辑操作
    val df: DataFrame = spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json")

    df.createOrReplaceTempView("user")

    spark.udf.register("ageAvg", functions.udaf(new MyAvgUDAF2()))

    //数据是一行一行进入，然后再返回一个计算好的平均值
    spark.sql("select ageAvg(age) from user").show


    //todo 关闭环境
    spark.stop()
  }
}

/*
* 自定义聚合函数类：计算年龄的平均值
* 1.继承org.apache.spark.sql.expressions.Aggregator，并定义泛型
*   IN:输入的数据类型,进来的是age，而且是Long类型
*   BUF:缓冲区的数据类型，看自己需要来定
*   OUT:输出的数据类型，返回一个平均值
*
* 2.重写方法
* */
case class Buff(var total: Long, var count: Long)

class MyAvgUDAF2 extends Aggregator[Long, Buff, Long] {
  //z & zero :缓冲区的初始化
  override def zero: Buff = {
    Buff(0L, 0L)
  }

  //根据输入的数据来更新缓冲区的数据
  override def reduce(buff: Buff, in: Long): Buff = {
    //增加年龄总和
    buff.total = buff.total + in
    //增加人数总和
    buff.count = buff.count + 1L
    buff
  }

  //合并缓冲区,因为可能有多个缓冲区
  override def merge(b1: Buff, b2: Buff): Buff = {
    //age的总和
    b1.total = b1.total + b2.total
    //age的个数
    b1.count = b1.count + b2.count
    b1
  }

  override def finish(resBuff: Buff): Long = {
    //计算平均值
    resBuff.total / resBuff.count
  }

  //缓冲区的编码操作,若是自定义的类比如Buff,那就用product
  override def bufferEncoder: Encoder[Buff] = Encoders.product

  //输出的编码操作,若是scala自带的类，那就用scalaXXX
  override def outputEncoder: Encoder[Long] = Encoders.scalaLong
}

























































