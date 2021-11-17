package com.atguigu.rdd.persist

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel

/**
 * @ClassName ScalaDemo-rdd_persist_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日19:27 - 周一
 * @Describe 持久化
 */
object rdd_persist_1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val list: List[String] = List("hello spark", "hello scala")

    val rdd: RDD[String] = sc.makeRDD(list)
    val flatRdd: RDD[String] = rdd.flatMap(_.split(" "))

    val mapRdd: RDD[(String, Int)] = flatRdd.map(x => {
      println("执行中。。")
      (x, 1)
    })

    //在这里缓存,RDD中数据就不会丢失,别的算子就可以接着这个继续用
    //可以放在内存也可以放在磁盘
    //缓存不是立即缓存的，只有在触发行动算子时才会缓存，因为只有这样才会有数据
    mapRdd.cache()//默认存内存
    mapRdd.persist(StorageLevel.DISK_ONLY)//默认存磁盘,临时文件,Job执行完后会删除

    val reduceRdd: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)
    //触发缓存逻辑
    reduceRdd.collect().foreach(println)

    println("---------------------------------")

    //这个算子可以接着上次缓存的地方继续使用
    val groupRdd: RDD[(String, Iterable[Int])] = mapRdd.groupByKey()
    groupRdd.collect().foreach(println)

    sc.stop()
  }
}



































