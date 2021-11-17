package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_trans_6 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日20:55 - 周五
 * @Describe flatMap
 */
object rdd_trans_6 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: List[List[Int]] = List(List(1, 2), List(3, 4))

    val flatRdd: List[Int] = rdd.flatten

    sc.stop()
  }
}

object rdd_flatMap_6_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //切分字符串
    val rdd: RDD[String] = sc.makeRDD(List("hello hello", "world flower"))

    val flatRdd: RDD[String] = rdd.flatMap(_.split(" "))

    flatRdd.collect().foreach(println)
    sc.stop()
  }
}

object rdd_flatMap_6_3 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Any] = sc.makeRDD(List(List(1, 2), 3, List(4, 5)))

    rdd.flatMap(
      t => {
        t match {
          case list: List[_] => list
          case i: Int => List(i)
        }
      })

    //简化版,一个case其实就是一个匿名函数
    //且当方法中只传一个参数时用{}可以替代()
    rdd.flatMap{
      case list: List[_] => list
      case i: Int => List(i)
    }


    sc.stop()
  }
}
