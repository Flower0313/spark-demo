package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-streaming_resume_16 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日23:57 - 周二
 * @Describe
 */
object streaming_resume_16 {
  def main(args: Array[String]): Unit = {
    //使用检查点恢复数据，如果里面没有数据那就创建新对象
    val ssc: StreamingContext = StreamingContext.getActiveOrCreate("output/cp2", () => {
      val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

      val ssc = new StreamingContext(sparkConf, Seconds(5))

      val lines = ssc.socketTextStream("hadoop102", 9999)

      lines.print()

      ssc
    })


    //将对象存入检查点
    ssc.checkpoint("output/cp2")

    ssc.start()
    ssc.awaitTermination()


  }
}
