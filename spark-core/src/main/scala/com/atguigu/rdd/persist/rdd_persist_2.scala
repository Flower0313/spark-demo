package com.atguigu.rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_persist_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日20:04 - 周一
 * @Describe 查看缓存后血缘关系图
 */
object rdd_persist_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello spark", "hello scala"))

    //3.1.业务逻辑
    val wordRdd: RDD[String] = rdd.flatMap(line => line.split(" "))

    val wordToOneRdd: RDD[(String, Int)] = wordRdd.map {
      word => {
        println("************")
        (word, 1)
      }
    }

    // 采用reduceByKey，自带缓存
    val wordByKeyRDD: RDD[(String, Int)] = wordToOneRdd.reduceByKey(_ + _)

    //3.5 cache操作会增加血缘关系，不改变原有的血缘关系
    println(wordByKeyRDD.toDebugString)

    wordByKeyRDD.cache()

    //3.2 触发执行逻辑
    wordByKeyRDD.collect()

    println("-----------------")
    println(wordByKeyRDD.toDebugString)

    sc.stop()
  }

}
