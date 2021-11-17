package com.atguigu.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-Spark01_WordCount
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月20日0:36 - 周三
 * @Describe
 */
object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    //todo 建立和spark框架的连接
    //jdbc:Connection
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(sparkConf)

    //todo 执行业务操作

    //1.读取文件，以行为单位读取
    //hello hello world
    val lines: RDD[String] = sc.textFile("datas/source/1.txt", 3)

    //2.将一行数据进行拆分，形成一个个单词
    //hello hello world
    val words: RDD[String] = lines.flatMap(x => x.split(" "))

    val words2: RDD[String] = lines.flatMap(s => s.split(" "))


    //3.将数据根据单词进行分组
    //((hello->(hello,hello)),(world->(world)))
    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy(word => word)

    //4.对分组后的数据进行转换
    //(hello,2),(world,1)
    val wordToCount: RDD[(String, Int)] = wordGroup.map(x => (x._1, x._2.size))


    //5.将转换结果采集到控制台打印出来
    val tuples: Array[(String, Int)] = wordToCount.collect()
    /*
    * (hello,6)
    * (world,4)
    * */

    tuples.foreach(println)

    Thread.sleep(Int.MaxValue)
    //todo 关闭连接
    sc.stop()
  }
}
