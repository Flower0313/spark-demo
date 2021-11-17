package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-wordCount_10 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日9:12 - 周一
 * @Describe
 */
object wordCount_10 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    //以首字母来分组
    val first: RDD[(String, Int)] = rdd.flatMap(_.split(" "))
      .map { x => (x.take(1), 1)}
      .reduceByKey(_ + _)

    first.collect().foreach(println)

    sc.stop()
  }
}
