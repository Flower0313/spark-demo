package com.atguigu.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-CreateRdd_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日10:57 - 周五
 * @Describe 从集合中创建RDD
 */
object CreateRdd_1 {
  def main(args: Array[String]): Unit = {
    //1.创建sparkConf,local[*]表示模拟当前电脑最大可用核数
    val conf: SparkConf = new SparkConf().setAppName("rdd1").setMaster("local[*]")

    //2.创建sparkContext
    val sc = new SparkContext(conf)


    /**
     * 3.使用parallelize()创建rdd
     * parallelize:并行，有几个核数就可以几个并行
     * @param1 一个seq集合,要传，Array是Seq的子类
     * @param2 默认值参数,可不传
     * @param3 隐式参数,可不传
     */
    val rdd:RDD[Int] = sc.parallelize(Array(1,2,3,4,5,6,7,8));

    rdd.collect().foreach(println)

    //4.使用makeRDD()创建rdd,makeRDD内层是parallelize，但又不完全相同
    val rdd2: RDD[String] = sc.makeRDD(Array("a", "c", "b"))

    rdd2.collect().foreach(println)

    sc.stop()

  }
}
