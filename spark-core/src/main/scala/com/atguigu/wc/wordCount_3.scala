package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-wordCount_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日1:24 - 周一
 * @Describe 第三种wordCount
 */
object wordCount_3 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    val res: RDD[(String, Int)] = rdd.flatMap(_.split(" "))
      .map(x => (x, 1))
      .reduceByKey(_ + _)

    res.collect().foreach(println)


    sc.stop()
  }
}
