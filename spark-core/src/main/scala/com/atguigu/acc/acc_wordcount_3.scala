package com.atguigu.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

/**
 * @ClassName ScalaDemo-acc_wordcount_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日0:08 - 周二
 * @Describe
 */
object acc_wordcount_3 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello","hello","scala"))

    //准备累加器
    val accumulator: MyAccumulator = new MyAccumulator
    //注册在环境中
    sc.register(accumulator, "wordCountAcc")

    //累加器只能在foreach行动算子里面
    rdd.foreach(
      word => {
        accumulator.add(word)
      }
    )

    println(accumulator.value)

    sc.stop()
  }
}

/*
* 自定义累加器
*
* 1.继承AccumulatorV2,定义泛型
*   IN:累加器输入的数据类型
*   OUT:累加器返回的数据类型
*
*
*
* */
class MyAccumulator extends AccumulatorV2[String, mutable.Map[String, Long]] {

  private var wcMap = mutable.Map[String, Long]()

  //判断是否为初始状态
  override def isZero: Boolean = {
    //为空则为初始状态
    wcMap.isEmpty
  }

  override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
    new MyAccumulator()
  }

  override def reset(): Unit = {
    wcMap.clear()
  }

  //获取累加器需要计算的值
  override def add(word: String): Unit = {
    //查看wcMap是否有这个单词，有就给它值+1，没有就创建
    val newCnt: Long = wcMap.getOrElse(word, 0L) + 1
    wcMap.update(word, newCnt)
  }

  //Driver合并多个累加器
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
    val map1 = this.wcMap
    val map2 = other.value //获取other的wcMap

    map2.foreach {
      case (word, count) => {
        val newCount: Long = map1.getOrElse(word, 0L) + count
        map1.update(word, newCount)
      }
    }
  }

  //累加器结果
  override def value: mutable.Map[String, Long] = {
    wcMap
  }
}




































