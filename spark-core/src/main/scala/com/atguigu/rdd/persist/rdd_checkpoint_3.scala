package com.atguigu.rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_persist_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日20:04 - 周一
 * @Describe 检查点 -- 需要落盘
 */
object rdd_checkpoint_3 {
  def main(args: Array[String]): Unit = {


    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    //设置checkPoint点保存路径
    sc.setCheckpointDir("datas/checkpoint")
//    sc.setCheckpointDir("hdfs://hadoop102:8020/ck")

    val list: List[String] = List("hello spark", "hello scala")

    val rdd: RDD[String] = sc.makeRDD(list)
    val flatRdd: RDD[String] = rdd.flatMap(_.split(" "))

    val mapRdd: RDD[(String, Int)] = flatRdd.map(x => {
      println("执行中。。") //为啥执行了8次，因为checkpoint也会单独开个任务执行一遍,然后存储起来，下次使用自己从这拿
      (x, 1)
    })


    //需要设置路径来落盘,Job执行完后不会被删除
    //一般保存路径都在hdfs中
    mapRdd.checkpoint()

    val reduceRdd: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)
    
    println(reduceRdd.toDebugString)
  
    //触发缓存逻辑
    reduceRdd.collect().foreach(println)

    //检查点切断了血缘关系
    println("---------------------------------")

    //这里直接从checkpoint点开始继承血缘关系，前面的血缘关系都断开了
    val groupRdd: RDD[(String, Iterable[Int])] = mapRdd.groupByKey()
    println(groupRdd.toDebugString)
    groupRdd.collect().foreach(println)

    sc.stop()
  }
}
