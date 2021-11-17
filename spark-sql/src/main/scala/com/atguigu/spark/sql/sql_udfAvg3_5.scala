package com.atguigu.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, Row, SparkSession, TypedColumn, functions}
import org.apache.spark.sql.expressions.Aggregator

/**
 * @ClassName ScalaDemo-sql_aggregate_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日20:00 - 周六
 * @Describe 聚合算平均值的方式三
 */
object sql_udfAvg3_5 {
  def main(args: Array[String]): Unit = {

    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 执行逻辑操作
    val df: DataFrame = spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json")

    df.createOrReplaceTempView("user")

    //早期版本中，spark不能在sql中使用强类型UDAF操作
    //早期的UDAF强类型聚合函数使用DSL语法

    val ds: Dataset[User2] = df.as[User2]

    //将UDAF函数转换为列对象
    val udaf: TypedColumn[User2, Long] = new MyAvgUDAF3().toColumn

    //将一行当成对象
    ds.select(udaf).show()

    //todo 关闭环境
    spark.stop()
  }
}

/*
* 自定义聚合函数类：计算年龄的平均值
* 1.继承org.apache.spark.sql.expressions.Aggregator，并定义泛型
*   IN:输入的数据类型User
*   BUF:缓冲区的数据类型，看自己需要来定
*   OUT:输出的数据类型User
*
* 2.重写方法
* */
case class User2(name: String, age: Long)

case class Buff2(var total: Long, var count: Long)

class MyAvgUDAF3 extends Aggregator[User2, Buff2, Long] {
  //z & zero :缓冲区的初始化
  override def zero: Buff2 = {
    Buff2(0L, 0L)
  }

  //根据输入的数据来更新缓冲区的数据
  override def reduce(buff: Buff2, in: User2): Buff2 = {
    //增加年龄总和
    buff.total = buff.total + in.age
    //增加人数总和
    buff.count = buff.count + 1L
    buff
  }

  //合并缓冲区,因为可能有多个缓冲区
  override def merge(b1: Buff2, b2: Buff2): Buff2 = {
    //age的总和
    b1.total = b1.total + b2.total
    //age的个数
    b1.count = b1.count + b2.count
    b1
  }

  override def finish(resBuff: Buff2): Long = {
    //计算平均值
    resBuff.total / resBuff.count
  }

  //缓冲区的编码操作,若是自定义的类比如Buff,那就用product
  override def bufferEncoder: Encoder[Buff2] = Encoders.product

  //输出的编码操作,若是scala自带的类，那就用scalaXXX
  override def outputEncoder: Encoder[Long] = Encoders.scalaLong
}

























































