package com.atguigu.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_save_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日11:02 - 周一
 * @Describe save
 */
object rdd_save_1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3), ("d", 4)), 2)

    rdd.saveAsTextFile("output/output1")
    rdd.saveAsObjectFile("output/output2")
    rdd.saveAsSequenceFile("output/output3")

    sc.stop()
  }
}
