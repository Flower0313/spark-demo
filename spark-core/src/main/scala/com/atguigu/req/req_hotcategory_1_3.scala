package com.atguigu.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-req_hotcategory_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日19:10 - 周二
 * @Describe 方式一2.0
 */
object req_hotcategory_1_3 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //1.读数据
    val rdd: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")

    //先统一数据结构，这样reduceByKey会先在分区内相同key的合并，就不会分区间去合并了，减少shuffle量
    val resRdd: Array[(String, (Int, Int, Int))] = rdd.flatMap {
      x => {
        val datas: Array[String] = x.split("_")
        if (datas(6) != "-1") {
          //点击量
          Array((datas(6), (1, 0, 0))) //flatMap返回的就是集合,才能被扁平化
        } else if (datas(8) != "null") {
          //下单量
          val chips: Array[String] = datas(8).split(",")
          chips.map(x => (x, (0, 1, 0)))//这里使用的scala的map而不是RDD的,返回的也是Array(String,(Int,Int,Int))
        } else if (datas(10) != "null") {
          //支付量
          val chips: Array[String] = datas(10).split(",")
          chips.map((_, (0, 0, 1)))//简化写法
        } else {
          Nil
        }
      }
    }.reduceByKey((u1, u2) => {//根据id来分组聚合
      (u1._1 + u2._1, u1._2 + u2._2, u1._3 + u2._3)
    }).sortBy(x => x._2, ascending = false).take(10)//因为比的是三元组，所以会调用Ordering的Tuples3方法

    resRdd.foreach(println)

    Thread.sleep(Int.MaxValue)

    sc.stop()
  }
}
