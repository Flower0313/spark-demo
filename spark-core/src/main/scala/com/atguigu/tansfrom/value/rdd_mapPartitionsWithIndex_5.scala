package com.atguigu.tansfrom.value

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_trans_5 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日20:21 - 周五
 * @Describe mapPartitionsWithIndex()分区索引
 */
object rdd_trans_5 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)

    //他会给分区加上索引，好分辨不同的分区
    val value = rdd.mapPartitionsWithIndex((index, iter) => {
      if (index == 0) {
        iter
      } else {
        //返回一个空迭代器
        Nil.iterator
      }
    })

    value.collect().foreach(println)


    sc.stop()
  }
}

object rdd_mapPartitionsWithIndex_5_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(List(1, 2, 3, 4))

    //需求：将每个元素前面打上它的分区号
    //先进入到每个分区集合，然后进入到集合的具体数据中，给该数据前面拼接上分区号
    val mapRdd = rdd.mapPartitionsWithIndex((index, iter) =>
      iter.map(x => (index, x))
    )
    //简化写法
    rdd.mapPartitionsWithIndex((x, y) => y.map((x, _)))

    mapRdd.foreach(println)

    sc.stop()
  }
}