package com.atguigu.rdd.depend

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_dep_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日15:24 - 周一
 * @Describe
 */
object rdd_dep_1 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd1: RDD[String] = sc.textFile("datas/1.txt")
    //HadoopRDD=>MapPartitionsRDD
    println(rdd1.toDebugString) //打印血源关系
    println("----------------------------")

    val rdd2: RDD[String] = rdd1.flatMap(_.split(" "))
    println(rdd2.toDebugString) //打印血源关系
    println("----------------------------")

    val rdd3: RDD[(String, Iterable[String])] = rdd2.groupBy(x => x)
    //MapPartitionsRDD=>ShuffledRDD
    println(rdd3.toDebugString) //打印血源关系
    println("----------------------------")

    val rdd4: RDD[(String, Int)] = rdd3.mapValues(x => x.size)
    //ShuffledRDD=>MapPartitionsRDD
    println(rdd4.toDebugString) //打印血源关系
    println("----------------------------")

    rdd4.collect().foreach(println)

    sc.stop()
  }
}

object rdd_dep_1_2 {
  /*
  * 依赖关系指的就是走不走shuffle，0号分区对应0号分区，1号对应1号这就是不走shuffle，是一对一依赖
  * */
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd1: RDD[String] = sc.textFile("datas/1.txt")
    println(rdd1.dependencies) //打印相邻两个RDD依赖关系
    println("----------------------------")

    val rdd2: RDD[String] = rdd1.flatMap(_.split(" "))
    println(rdd2.dependencies) //打印相邻两个RDD依赖关系
    println("----------------------------")

    val rdd3: RDD[(String, Iterable[String])] = rdd2.groupBy(x => x)
    println(rdd3.dependencies) //打印相邻两个RDD依赖关系
    println("----------------------------")

    val rdd4: RDD[(String, Int)] = rdd3.mapValues(x => x.size)
    println(rdd4.dependencies) //打印相邻两个RDD依赖关系
    println("----------------------------")

    /*
    * reduceByKey不一定走shuffle，因为会判断上一个rdd的分区器是否一样，如果一样就不走shuffle了，通用。
    * 因为连续两次shuffle没意义
    * */
    sc.stop()
  }
}
