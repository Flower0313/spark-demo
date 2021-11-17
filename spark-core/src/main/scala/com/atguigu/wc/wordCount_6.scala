package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-wordCount_6 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日1:29 - 周一
 * @Describe
 */
object wordCount_6 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    val res: collection.Map[String, Long] = rdd.flatMap(_.split(" "))
      .map(x => (x, 1))
      .countByKey()

    println(res)

    sc.stop()
  }
}
