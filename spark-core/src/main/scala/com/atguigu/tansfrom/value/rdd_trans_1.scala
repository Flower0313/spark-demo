package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_trans_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日18:53 - 周五
 * @Describe 单个的数据map算子
 */
object rdd_trans_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //1,2,3,4 => 2,4,6,8
    val mapRdd: RDD[Int] = rdd.map(_ * 2)

    mapRdd.collect().foreach(println)

    sc.stop()
  }
}
