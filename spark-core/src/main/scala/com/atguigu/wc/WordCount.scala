package com.atguigu.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-WordCount 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日17:51 - 周二
 * @Describe hdfs
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf = new SparkConf().setAppName("WC").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc = new SparkContext(conf)

    //一行搞定
    sc.textFile("datas/source/1.txt")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
    //value.collect().foreach(println) //元素打印是按hashcode%分区数的顺序
      .saveAsTextFile("output/output13")



    //8.关闭连接
    sc.stop()
  }
}
