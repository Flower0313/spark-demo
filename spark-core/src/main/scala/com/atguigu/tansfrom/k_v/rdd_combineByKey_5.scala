package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_foldKey_4 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日14:22 - 周日
 * @Describe 对agreegateByKey去掉初始值的写法
 */
object rdd_combineByKey_5 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ), 2)

    /*
    * combineByKey:方法需要三个参数
    * 参数1:将相同key的第一个数据进行结构的转换
    * 参数2:分区内的计算规则
    * 参数3:分区间的计算规则
    *
    * */
    val value = rdd.combineByKey(v => (v, 1),//这个v就是每个分区不同key的value值,但(v,1)类型不确定
      /*
      * 这里t为啥要声明类型呢?因为aggreegateByKey有初始值声明类型(有上下文限定ClassTag)，
      * 而这里的(v,1)是在运行中才能得到类型，所以需要我们手动来声明，
      * 故我们用(Int,Int)来约束(v,1)
      * */
      (t: (Int, Int), v) => {//v就是我们集合中的value值，它推断出来是Int类型，而(Int,Int)就是对(v,1)的类型的限定
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )
    value.collect().foreach(println)
    sc.stop()
  }
}

