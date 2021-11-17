package com.atguigu.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-CreateRdd_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日11:06 - 周五
 * @Describe 从文件中创建RDD
 */
object CreateRdd_2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("rdd2").setMaster("local")

    val sc = new SparkContext(conf)

    //若是集群路径就是hdfs://hadoop102:8020/input
    //path路径默认以当前环境的根路径为基准，可绝对也可相对,路径中也可以有通配符
    val line = sc.textFile("datas")

    line.foreach(println)

    sc.stop()

  }
}
