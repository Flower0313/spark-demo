package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-streaming_window_10 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日19:29 - 周二
 * @Describe windowOperations - 滑窗
 */
class streaming_window_10 {
  def main(args: Array[String]): Unit = {

    //需求：统计WordCount：3秒一个批次，窗口6秒
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lines = ssc.socketTextStream("hadoop102", 9999)

    val wordToOne: DStream[(String, Int)] = lines.map((_, 1))

    /*
    * 1.窗口的范围应该是采集周期的整数倍,因为不能采集到一半就结束啊
    * 2.窗口可以滑动，但在默认情况下，一个采集周期进行滑动(3)，那么这就会有重复数据，在中间的数据
    * 3.为避免重复数据，改变滑动步长,但有时又需要一部分重复数据，这就用reduceByKeyAndWindow
    * 4.如果有多批数据进入窗口,最终也会通过window操作变成统一的RDD处理
    * */

    //设置窗口范围为6s,滑动步长也为6s,这样就能去重
    val windowDS: DStream[(String, Int)] = wordToOne.window(Seconds(6), Seconds(6))

    val wordToCount: DStream[(String, Int)] = windowDS.reduceByKey(_ + _)

    wordToCount.print()

    ssc.start()
    ssc.awaitTermination()
  }
}

































