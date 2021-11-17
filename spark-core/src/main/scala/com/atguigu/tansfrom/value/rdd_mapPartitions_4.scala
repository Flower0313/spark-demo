package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_trans_4 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日19:37 - 周五
 * @Describe mapPartitions
 */
object rdd_trans_4 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)

    //map是一个分区内数据一个一个的取出，而mapPartitions是一分区一分区取出
    //它会将整个数据加载到内存中，如果处理完的数据是不会被释放掉的，因为存在对象的引用，在数据量大内存小时会溢出
    //由于mapPartitions取的是分区，所以x是一个分区集合，再对x中的单个元素使用map
    //其中x只会走两次，因为只有两个分区,而每个分区中的map会走2次，map一共走4次
    val mapPRdd: RDD[Int] = rdd.mapPartitions(x => {
      println("mapPartitions")
      x.map(y => {
        println("map")
        y * 2
      })
    })


    //local模式先不适用collect()
    mapPRdd.foreach(println)

    sc.stop()
  }
}

object rdd_mapPartitions_4_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //取出买个数据分区的最大值
    //【1,2】和【3,4】 ==> 【2】，【4】

    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)

    //先获取集合中最大值，然后再转换成iterator返回
    //从这里可以看出，mapPartitions可以改变集合中的数，这是map无法实现的
    val mapRdd = rdd.mapPartitions(x => List(x.max).iterator)

    mapRdd.foreach(println)

    sc.stop()
  }
}