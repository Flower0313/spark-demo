package com.atguigu.wc

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

/**
 * @ClassName ScalaDemo-wordCount_8 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日9:10 - 周一
 * @Describe
 */
object wordCount_8 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("xiao scala", "hello spark"))


    val mapRdd: RDD[mutable.Map[String, Long]] = rdd.flatMap(x => x.split(" "))
      .map(x => {
        mutable.Map((x, 1)) //要写两层括号
      })

    val reduceRdd = mapRdd.reduce((x, y) => {
      y.foreach {
        case (word, count) => {
          x.update(word, x.getOrElse(word, 0L) + count)
        }
      }
      x
    })
    reduceRdd.foreach(println)


    sc.stop()
  }
}
