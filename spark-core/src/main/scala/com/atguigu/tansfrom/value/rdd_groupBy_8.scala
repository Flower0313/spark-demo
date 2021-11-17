package com.atguigu.tansfrom.value

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_trans_8 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日9:31 - 周六
 * @Describe groupBy
 */
object rdd_trans_8 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(1 to 4, 1)

    /*
    * Q:这个groupBy是对每个分区中的元素分组，还是对整个元素进行分组？
    * A:是根据整个元素来分组，而不是在每个分区中进行分组
    * 比如：【1,2】、【3,4】以奇偶分组=>(偶,【2,4】)、(奇,【1,3】)
    *
    * */
    val value: RDD[(String, Iterable[Int])] = rdd.groupBy(x => if (x % 2 == 0) "偶" else "奇")
    value.saveAsTextFile("output/output1")

    value.collect().foreach(println)

    sc.stop()

  }
}

object rdd_groupBy_8_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(List("hello", "hive", "hadoop", "spark", "scala"))

    //根据首字母来分组
    val value: RDD[(String, Iterable[String])] = rdd.groupBy(x => x.take(1))

    //    value.collect().foreach(println)

    sc.stop()

  }
}

object rdd_groupBy_8_3 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //需求：从服务器日志数据 apache.log 中获取每个时间段访问量
    val rdd: RDD[String] = sc.textFile("datas/apache.log")

    val value: RDD[(String, String)] = rdd.groupBy(x => x.split(" ")(3).substring(0, 10))
      .map(x => ("时间:" + x._1, "个数:" + x._2.size))

    value.collect().foreach(println)
    sc.stop()
  }
}


object rdd_groupBy_8_4 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //需求：用groupBy实现wordCount
    val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark", "Hello World"))

    val resRdd: RDD[(String, Int)] = rdd.flatMap(x => x.split(" "))
      .groupBy(x => x)
      .map(x => (x._1, x._2.size))

    resRdd.collect().foreach(println)

    sc.stop()
  }
}