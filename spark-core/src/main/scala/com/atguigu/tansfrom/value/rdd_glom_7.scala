package com.atguigu.tansfrom.value

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_trans_7 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日23:30 - 周五
 * @Describe glom -- 将分区变成Array数组
 */
object rdd_trans_7 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //【1,2】、【3,4】
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //将每个分区集合转换成Array集合RDD(Array(1,2),Array(3,4))
    val glomRdd: RDD[Array[Int]] = rdd.glom()

    //取每个集合中最大值,RDD(Array(1,2),Array(3,4))==>RDD(2,4)
    val mapRdd: RDD[Int] = glomRdd.map(x => x.max)

    //求和
    val sum: Int = mapRdd.collect().sum

    println(sum)

    sc.stop()
  }
}

//经过glom算子之后分区数不会改变，且不同分区之间都是独立的
object rdd_glom_7_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //【1,2】、【3,4】
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    val mapRdd: RDD[Int] = rdd.map(_ * 2)

    //【1,2】、【3，4】=>【2,4】、【6,8】
    mapRdd.saveAsTextFile("output/output/1")
    sc.stop()
  }
}

object rdd_glom_7_3 {
  def main(args: Array[String]): Unit = {


    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //将分区的数据转换成string拼接在一起
    val rdd: RDD[String] = sc.makeRDD(List("a", "b", "c", "d"), 2)
    val value: RDD[String] = rdd.glom().map(t => t.foldLeft("")(_ + _))

    value.collect().foreach(println)

    sc.stop()
  }
}
