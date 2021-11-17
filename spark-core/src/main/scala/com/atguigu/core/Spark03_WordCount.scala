package com.atguigu.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-Spark01_WordCount
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月20日0:36 - 周三
 * @Describe 将log4j.properties改成log4j-default.properties再改回来就成功了
 */
object Spark03_WordCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(sparkConf)

    //todo 执行业务操作

    //1.读取文件，以行为单位读取
    //hello hello world
    val lines = sc.textFile("T:\\ShangGuiGu\\SparkDemo\\datas")

    val tuples = {
      lines.flatMap(_.split(" "))
        .map(x => (x, 1)) //(hello,1)(hello,1)(hello,1)(word,1)(word,1)(hello,1)(word,1)
        //相同key的数据对value进行聚合
        .reduceByKey((x, y) => x + y).collect() //分组聚合一个方法实现，(hello,4)(world,3)
    }
    tuples.foreach(println)
    sc.stop()
  }
}

object Spark03_WordCount_2 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(sparkConf)

    val lines = sc.textFile("T:\\ShangGuiGu\\ScalaDemo\\datas\\source\\1.txt", 3)

    val tuples = {
      lines.flatMap(_.split(" "))
        .map(x => (x, 1))
    }
    println("map时的分区方式：" + tuples.partitioner) //None

    val reduce: RDD[(String, Int)] = tuples.reduceByKey((x, y) => x + y) //默认走HashPartitioner
    reduce.collect().foreach(println)
    //tuples.collect().foreach(println)
    println("可以查看了")


    sc.stop()
  }
}


