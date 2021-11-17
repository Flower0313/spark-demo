package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_sortByKey_11 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日18:52 - 周三
 * @Describe
 */
object rdd_sortByKey_11 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(Int, String)] = sc.makeRDD(Array((3, "aa"), (6, "cc"), (2, "bb"), (1, "dd")), 2)

    //3.2 按照key的正序（默认顺序）
    rdd.sortByKey(true).collect().foreach(println)

    //3.3 按照key的倒序
    //rdd.sortByKey(false).collect().foreach(println)


    sc.stop()
  }

}
