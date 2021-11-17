package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-streaming_reduceByKeyAndWindow_11 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日20:09 - 周二
 * @Describe reduceByKeyAndWindow有状态计算
 */
object streaming_reduceByKeyAndWindow_11 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    val ssc = new StreamingContext(sparkConf, Seconds(3))

    ssc.checkpoint("output/cp1")

    val lines = ssc.socketTextStream("hadoop102", 9999)

    val wordToOne: DStream[(String, Int)] = lines.map((_, 1))

    /*
    * 如果需要用到重复的数据，但又不需要完全重复计算,这就需要用到这个
    * 当窗口范围比较大，但是滑动幅度比较小，那么可以采用增加数据和删除数据的方式
    * 无需重复计算
    * */
    val windowsDS: DStream[(String, Int)] = wordToOne.reduceByKeyAndWindow(
      (x: Int, y: Int) => {//新进窗口的元素就加上
        x + y
      },
      (x: Int, y: Int) => {//移除窗口的元素就减去
        x - y
      },
      Seconds(9), //窗口大小9秒
      Seconds(3) //步长3秒
    )

    windowsDS.print()


    ssc.start()
    ssc.awaitTermination()
  }
}






















































