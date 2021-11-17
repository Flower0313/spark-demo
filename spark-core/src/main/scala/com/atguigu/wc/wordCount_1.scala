package com.atguigu.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-wordCount_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日1:12 - 周一
 * @Describe 第一种wordcount
 */
object wordCount_1 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    val res: RDD[(String, Int)] = rdd.flatMap(_.split(" "))
      .groupBy(x => x)
      .mapValues(x => x.size)

    res.collect().foreach(println)
    sc.stop()
  }
}
