package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_reduceBykey_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日1:22 - 周日
 * @Describe reduceBykey
 */
object rdd_reduceBykey_2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3), ("b", 4)
    ))

    //相同的key来做聚合
    //a->【1,2,3】 b->【4】
    /*
    * (x,y) => x+y
    * x和y都是表示相同key的不同value，1和2先聚合成3然后再和3聚合成6
    *
    * 结果(a,6)(b,4)
    *
    * 如果reduceBykey中key的数据只有一个，是不会参与运算的，比如这里b只有一个
    *
    * 若有分区的话，先进行分区内计算，再计算分区间，两次计算reduceByKey的初始值都是第一个元素
    * */
    val reduceRdd: RDD[(String, Int)] = rdd.reduceByKey((x, y) => {
      println("x=" + x, ",y=" + y)
      x + y
    })

    reduceRdd.collect().foreach(println)

    sc.stop()
  }
}

object rdd_reduceBykey_2_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3), ("a", 2)
    ), 2)

    /*
    * 若有分区的话，先进行分区内计算，再计算分区间，两次计算reduceByKey的初始值都是第一个元素
    * 分区内计算：【1】-【2】=【-1】,【3】-【2】=【1】
    * 分区间计算：【-1-1】=-2，最后结果是【a,-2】
    * */
    val reduceRdd: RDD[(String, Int)] = rdd.reduceByKey(_ - _)

    reduceRdd.collect().foreach(println)

    sc.stop()
  }
}
