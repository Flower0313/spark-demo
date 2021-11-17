package com.atguigu.homework

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-day_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日17:50 - 周一
 * @Describe 需求说明: 读取文件1.txt ,
 *           求出文件中各个单词首字母的个数.(hello spark hello spark scala) => (h,2),(s,3)
 */
object day_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.textFile("datas/1.txt")

    val resRdd: RDD[(Char, Int)] = rdd.flatMap(_.split(" "))
      .groupBy(_.head)
      .map(x => (x._1, x._2.size))

    println(resRdd)

    sc.stop()
  }
}
