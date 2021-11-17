package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_trans_9 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日10:55 - 周六
 * @Describe filter
 */
object rdd_trans_9 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    val filRdd: RDD[Int] = rdd.filter(x => x % 2 == 0)

    filRdd.collect().foreach(println)

    sc.stop()
  }
}

object rdd_filter_9_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.textFile("datas/apache.log")

    //获取apache.log中1998年3月13号的请求路径
    val strings1: RDD[String] = rdd
      .filter(x => x.split(" ")(3).startsWith("13/03/1998"))

    strings1.collect().foreach(println)

    sc.stop()
  }
}
