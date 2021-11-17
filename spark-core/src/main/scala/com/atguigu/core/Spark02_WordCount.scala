package com.atguigu.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-Spark01_WordCount
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月20日0:36 - 周三
 * @Describe
 */
object Spark02_WordCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(sparkConf)

    //todo 执行业务操作

    //1.读取文件，以行为单位读取
    //hello hello world
    val lines = sc.textFile("T:\\ShangGuiGu\\SparkDemo\\datas")

    val value: RDD[(String, Int)] = lines
      //相当于先将集合(hello,hello,world)再((hello,1),(hello,1),(world,1))
      .flatMap(x => x.split(" ").map(y => (y, 1)))
      .groupBy(x => x._1)
      .map(x => (x._1, x._2.size))

    value.collect().foreach(println)
    sc.stop()
  }
}
