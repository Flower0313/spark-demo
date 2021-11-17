package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
 * @ClassName ScalaDemo-streaming_join_9 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日19:21 - 周二
 * @Describe
 */
class streaming_join_9 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    //2.初始化SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //接收数据，是SparkStreaming的DStream
    val dataA = ssc.socketTextStream("hadoop102", 9999)

    val dataB = ssc.socketTextStream("hadoop102", 8888)

    val mapA = dataA.map((_, 9))
    val mapB = dataB.map((_, 8))

    val joinDS: DStream[(String, (Int, Int))] = mapA.join(mapB)

    //底层就是rdd的join
    joinDS.print()


    ssc.start()
    ssc.awaitTermination()
  }
}
