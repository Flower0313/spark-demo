package com.atguigu.tansfrom.two_value

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_all 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日22:39 - 周六
 * @Describe 差集、并集、交集、拉链
 */
object rdd_all {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    /*
    * 注意事项：
    * 1.交集、并集、差集的数据类型要保持一致，zip没有这个要求
    *
    *
    * */
    val rdd1 = sc.makeRDD(1 to 4, 2)
    val rdd2 = sc.makeRDD(4 to 9, 3)
    val rdd3 = sc.makeRDD(List("3", "4", "9", "10"))

    val tuples: List[(String, (Int, Int))] = List(("a", (13, 31))).union(List(("b", (12, 21))))
    println(tuples)
    

    //交集//4
    /*
    * 交集走shuffle，使用hash分区器，最终分区个数是两个rdd中分区个数比较多的
    * */
    println("交集")
    //rdd1.intersection(rdd2).collect().foreach(print)

    //并集//12344567
    println("并集")
    /*
    * 原有分区保持不变，不走shuffle，分区数等于两个rdd的分区数和
    * */
    //rdd1.union(rdd2).saveAsTextFile("output/output1")

    //差集(看站在谁的角度上执行)
    /*
    * 走shuffle，使用hash分区器
    * 分区个数保持不变
    * */
    //println("差集") //123
    //rdd1.subtract(rdd2).collect().foreach(print)

    //拉链//(1,4)(2,5)(3,6)(4,7)
    println("拉链")
    //rdd1.zip(rdd2).collect().foreach(print)
    //rdd1.zip(rdd3).collect().foreach(print) //(1,3)(2,4)(3,9)(4,10)

    sc.stop()
  }
}


object rdd_all_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    //【1,2】【3,4】
    val rdd1: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4),2)
    //【3】【4】【5】【6】
    val rdd2 = sc.makeRDD(List(3, 4, 5, 6),4)

    //分区数量相等且分区中个数也要相等
    rdd1.zip(rdd2).collect().foreach(println)


    sc.stop()
  }
}
