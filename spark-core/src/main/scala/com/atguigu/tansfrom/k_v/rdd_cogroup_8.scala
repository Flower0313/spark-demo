package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_cogroup_8 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日20:35 - 周日
 * @Describe cogroup
 */
object rdd_cogroup_8 {
  def main(args: Array[String]): Unit = {

    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc:SparkContext = new SparkContext(conf)

    //cogroup = connect + group
    val rdd1: RDD[(String, Int)] =
      sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3),("e",13),("a",313)))

    val rdd2: RDD[(String, Int)] =
      sc.makeRDD(List( ("b", 5), ("c", 6),("a", 4),("a",100)))

    //有几个数据源就有几个Iterable[Int]
    val coRdd: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)

    coRdd.collect().foreach(println)
    /*
    * (a,(CompactBuffer(1, 313),CompactBuffer(4, 100)))
    * (b,(CompactBuffer(2),CompactBuffer(5)))
    * (c,(CompactBuffer(3),CompactBuffer(6)))
    * (e,(CompactBuffer(13),CompactBuffer())),rdd2中没有e,则会为空集合
    * */
    sc.stop()
  }
}





















