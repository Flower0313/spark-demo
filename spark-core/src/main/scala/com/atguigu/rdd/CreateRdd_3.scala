package com.atguigu.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-CreateRdd_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日11:34 - 周五
 * @Describe
 */
object CreateRdd_3 {
  def main(args: Array[String]): Unit = {
    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    
    val sc:SparkContext = new SparkContext(conf)

    //这个方法会标明哪些数据来自哪些文件，以元组形式返回
    //textFiles是以行为单位读取数据
    //wholeTextFiles是以文件为单位读取数据
    val line = sc.wholeTextFiles("datas")

    line.foreach(println)
    
    sc.stop()
  }

}
