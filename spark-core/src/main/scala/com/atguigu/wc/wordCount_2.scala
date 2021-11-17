package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-wordCount_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日1:20 - 周一
 * @Describe 第二种wordCount
 */
object wordCount_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    val res: RDD[(String, Int)] = rdd.flatMap(_.split(" "))
      .map(x => (x, 1))
      .groupByKey() //核心方法
      .mapValues(x=>x.size) //只改变values值

    res.collect().foreach(println)


    sc.stop()
  }
}
