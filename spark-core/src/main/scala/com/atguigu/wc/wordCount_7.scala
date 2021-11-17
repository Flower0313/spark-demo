package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-wordCount_7 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日1:30 - 周一
 * @Describe
 */
object wordCount_7 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    val res= rdd.flatMap(_.split(" "))
      .countByValue()

    println(res)

    sc.stop()
  }
}
