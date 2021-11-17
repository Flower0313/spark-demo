package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
 * @ClassName ScalaDemo-streaming_ds_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月31日16:07 - 周日
 * @Describe DStream的创建
 */
object streaming_ds_2 {
  def main(args: Array[String]): Unit = {
    //1.初始化Spark配置信息
    val conf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    val name: String = "flower"

    //2.初始化ssc
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))

    //3.创建RDD队列
    val rddQueue: mutable.Queue[RDD[Int]] = new mutable.Queue[RDD[Int]]()

    //4.
    val inputDStream = ssc.queueStream(rddQueue, oneAtATime = false)
    val mappedStream = inputDStream.map(x => {
      println("我会每次执行..." + name)//为什么集群模式中不会打印呢
    })
    //val reduceStream: DStream[(Int, Int)] = mappedStream.reduceByKey(_ + _)


    //6.打印结果
    mappedStream.print()

    //7.启动任务
    ssc.start()

    //8.循环创建并向RDD队列中放入RDD
    for (i <- 1 to 5) {
      rddQueue += ssc.sparkContext.makeRDD(1 to 2)
      Thread.sleep(2000)
    }

    //休眠
    ssc.awaitTermination()//不开启只会执行一次


  }
}
