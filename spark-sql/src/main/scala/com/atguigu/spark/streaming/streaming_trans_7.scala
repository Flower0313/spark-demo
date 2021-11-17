package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
 * @ClassName ScalaDemo-streaming_trans_7 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日19:09 - 周二
 * @Describe transform介绍
 */
object streaming_trans_7 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    //初始化SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)

    println("开始执行map")//Driver端执行,所以只会执行一次
    val oldDS: DStream[String] = lines.map {
      x => {
        println("单纯map")//Executor端执行
        x
      }
    }

    println("开始执行transform的map")//Driver端执行(只执行一次)
    val newDS: DStream[String] = lines.transform(
      rdd => {
        println("已进入到transform")//Driver端执行(周期性执行,也就是一批次一次)，因为在RDD算子之外
        rdd.map{
          x=>{
            println("transform中的map")//Executor端执行
            x
          }
        }
      }
    )
    oldDS.print()
    newDS.print()

    ssc.start()
    ssc.awaitTermination()
    /*
    * 总结：transform的使用场景
    * 1.DStream不完善
    * 2.需要代码周期性执行
    * */


  }
}
