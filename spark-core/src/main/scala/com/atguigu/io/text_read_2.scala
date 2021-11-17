package com.atguigu.io

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-text_read_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日21:21 - 周一
 * @Describe Text文件 -- 读取文件
 */
object text_read_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd1 = sc.textFile("output/output1")
    println(rdd1.collect().mkString(","))

    val rdd2 = sc.objectFile[(String,Int)]("output/output2")
    println(rdd2.collect().mkString(","))

    val rdd3 = sc.sequenceFile[String,Int]("output/output3")
    println(rdd3.collect().mkString(","))

    sc.stop()
  }
}
