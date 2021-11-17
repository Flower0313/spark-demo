package com.atguigu.rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_persist_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日20:04 - 周一
 * @Describe 检查点与缓存联合使用
 */
object rdd_checkpoint_4 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    //设置checkPoint点保存路径
    sc.setCheckpointDir("datas/checkpoint")

    val list: List[String] = List("hello spark", "hello scala")

    val rdd: RDD[String] = sc.makeRDD(list)
    val flatRdd: RDD[String] = rdd.flatMap(_.split(" "))

    val mapRdd: RDD[(String, Int)] = flatRdd.map(x => {
      println("执行中。。") //使用cache和checkpoint联合起来就执行一次，不用打印8个了
      (x, 1)
    })

    mapRdd.cache()
    mapRdd.checkpoint()//直接保存缓存中的RDD，然后就不需要自己再执行一次了

    val reduceRdd: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)

    println(reduceRdd.toDebugString)

    reduceRdd.collect().foreach(println)

    println("---------------------------------")

    Thread.sleep(Int.MaxValue)
    //这里直接从checkpoint点开始继承血缘关系，前面的血缘关系都断开了
    /*val groupRdd: RDD[(String, Iterable[Int])] = mapRdd.groupByKey()
    groupRdd.collect().foreach(println)*/

    sc.stop()
  }
}
