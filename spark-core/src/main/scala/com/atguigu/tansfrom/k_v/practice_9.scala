package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-practice_9 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日20:56 - 周日
 * @Describe 统计每个省广告被点击次数top3
 */
object practice_9 {//简化写法
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val dataRDD: RDD[String] = sc.textFile("datas/source/agent.log")

    val resRdd: RDD[(String, List[(String, Int)])] = dataRDD
      .map { line => (line.split(" ")(1) + "-" + line.split(" ")(4), 1) } //将原始数据进行结构转换string =>(省份-广告,1)
      .reduceByKey(_ + _) //对其相同的key进行聚合，也就是计算个数，(省份-广告,sum)
      .map { case (k, v) => (k.split("-")(0), (k.split("-")(1), v)) } //改变key，(省份，(广告,sum))
      .groupByKey() //按省份进行分组 (省份,【广告A,sum】,【广告B,sum】)
      .mapValues(
        x => x.toList.sortWith((l, r) => l._2 > r._2).take(3) //取每个省中的最大的前三条
      )

    resRdd.collect().foreach(println)

    sc.stop()
  }
}

object practice_9_2 {//完整写法
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val dataRDD: RDD[String] = sc.textFile("datas/source/agent.log")

    //将数据变成【(省份-广告),1】
    val mapRdd: RDD[(String, Int)] = dataRDD.map(x => (x.split(" ")(1) + "-" + x.split(" ")(4), 1))

    //将数据变成【(省份-广告),sum】
    val reduceRdd: RDD[(String, Int)] = mapRdd.reduceByKey(_ + _)

    //将数据变成【省份，(广告，sum)】
    val mapRDD2: RDD[(String, (String, Int))] =
      reduceRdd.map(x => (x._1.split("-")(0), (x._1.split("-")(1), x._2)))

    //将数据相同的省份key变成(省份,【广告A,sum】,【广告B,sum】)
    val groupRdd: RDD[(String, Iterable[(String, Int)])] = mapRDD2.groupByKey()

    //用mapValues只取value再对其进行排序
    val sortRdd: RDD[(String, List[(String, Int)])] =
      groupRdd.mapValues(x => x.toList.sortWith((l, r) => l._2 > r._2).take(3))

    sortRdd.collect().foreach(println)
    sc.stop()
  }
}


























