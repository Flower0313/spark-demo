package com.atguigu.homework

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-foreachPartition 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日21:23 - 周二
 * @Describe
 */
object foreachPartition {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val list: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    list.foreach(println)

    //这是按分区来遍历元素,
    list.foreachPartition(iter => println(iter.size))

    list.foreachPartition(x => x.foreach(println))
    sc.stop()
  }
}
