package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_trans_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日19:09 - 周五
 * @Describe 需求：取出apache.log中的url地址
 */
object rdd_trans_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc:SparkContext = new SparkContext(conf)


    val rdd: RDD[String] = sc.textFile("datas/apache.log",3)

    val mapRdd: RDD[String] = rdd.map(_.split(" ")(6))

    mapRdd.collect().foreach(println)

    sc.stop()
  }
}
