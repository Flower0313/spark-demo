package com.atguigu.rdd.depend

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_task_2
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日19:12 - 周一
 * @Describe
 */
object rdd_task_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    //Application:初始化一个SparkContext就是生成一个Application
    val sc: SparkContext = new SparkContext(conf)

    //这三个算子都没有shuffle过程,属于窄依赖,不会增加stage阶段
    val rdd: RDD[String] = sc.textFile("datas/1.txt")
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))

    //reduceByKey有shuffle过程，会使stage加1，此时有2个stage
    val rdd4: RDD[(String, Int)] = rdd3.reduceByKey(_ + _)

    //一个Action算子就会生成一个Job
    //Job0:输出到控制台,collect()是行动算子
    rdd4.collect().foreach(println)
    //Job1:输出到文件,saveAsTextFile也是行动算子
    rdd4.saveAsTextFile("output/output1")

    println("可以查看了")
    //阻塞线程，方便进入观察页面
    Thread.sleep(Long.MaxValue)

    sc.stop()
  }
}
