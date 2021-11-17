package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-WordCount 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月31日15:23 - 周日
 * @Describe
 */
object streaming_wc_1 {
  def main(args: Array[String]): Unit = {

    //todo 创建环境对象
    //1.初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //2.第二个参数是采集周期3秒，每3秒采集一次做统计分析,里面会创建SparkContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))


    //todo 逻辑处理
    //3.获取端口数据,一行一行的字符串
    /*
    * ReceiverInputDStream继承于InputDStream继承于DStream
    * */
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)
    //4.打散成单词
    val words: DStream[String] = lines.flatMap(_.split(" "))
    val value: DStream[(String, Int)] = words.map(x => {
      println("正在执行")
      (x, 1)
    })
    //5.自定义格式
    val wordToOne: DStream[(String, Int)] = words.map({
      x=>(x,1)
    })
    //6.聚合
    val wordToCount: DStream[(String, Int)] = wordToOne.reduceByKey(_ + _)

    //这个wordToCount应该将所以RDD算子封装好带过去了，才能每次都执行
    wordToCount.print()

    //启动读取
    ssc.start()
    println("开始读取...")
    //阻塞主线程,若你将远程的nc关闭也会报错
    ssc.awaitTermination()

    //todo 关闭环境
    ssc.stop()

  }
}
