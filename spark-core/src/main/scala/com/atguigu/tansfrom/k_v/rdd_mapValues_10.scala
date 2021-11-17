package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_mapValues_10 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日23:00 - 周日
 * @Describe
 */
object rdd_mapValues_10 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(List((1, List(1, 2, 3, 4, 5)), (1, List(1321, 34, 5)), (2, List(12, 4, 5))))

    //只会对v进行操作，操作之后的v还是与之前的k对应

    val value: RDD[(Int, List[Int])] = rdd.mapValues(x=>x.take(1))

    value.collect().foreach(println)


    sc.stop()
  }
}
