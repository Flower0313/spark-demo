package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-wordCount_4 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日1:25 - 周一
 * @Describe
 */
object wordCount_4 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))

    val res: RDD[(String, Int)] = rdd.flatMap(_.split(" "))
      .map(x => (x, 1))
      .aggregateByKey(0)(_ + _, _ + _)
      //foldByKey(0)(_+_)
    res.collect().foreach(println)


    sc.stop()
  }
}
