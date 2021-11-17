package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-streaming_trans_7 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日13:53 - 周二
 * @Describe transform
 */
object streaming_trans_8 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    //2.初始化SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //接收数据，是SparkStreaming的DStream
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)

    //在Driver端执行,全局一次
    println("11111：" + Thread.currentThread().getName)

    //DStream转换为RDD操作
    val wordToSumDStream: DStream[(String, Int)] = lineDStream.transform(
      rdd => {
        //在Driver端执行，一批次执行一次
        println("22222：" + Thread.currentThread().getName)
        val words: RDD[String] = rdd.flatMap(_.split(" "))

        val wordToOne: RDD[(String, Int)] = words.map(x => {
          //在Executor端执行，和单词个数相同
          println("33333：" + Thread.currentThread().getName)
          (x, 1)
        })
        val result: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
        result
      }
    )

    wordToSumDStream.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
