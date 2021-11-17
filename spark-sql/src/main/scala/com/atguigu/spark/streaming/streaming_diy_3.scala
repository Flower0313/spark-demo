package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.Random

/**
 * @ClassName ScalaDemo-streaming_diy_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月31日16:17 - 周日
 * @Describe 自定义采集器
 */
object streaming_diy_3 {
  def main(args: Array[String]): Unit = {
    //1.初始化Spark配置信息
    val conf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")
    //2.初始化ssc,会启动eventLoop
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val messageDS: ReceiverInputDStream[String] = ssc.receiverStream(new FlowerReceiver())

    messageDS.print()
    ssc.start()
    ssc.awaitTermination()

  }
}

/*
* 自定义数据采集器：
* 1.定义泛型,传递参数
*
* */
class FlowerReceiver extends Receiver[String](StorageLevel.MEMORY_ONLY) {
  private var flag = true

  override def onStart(): Unit = {
    //开启一个线程专门生产消息
    new Thread(new Runnable {
      override def run(): Unit = {
        while (flag) {
          val message: String = "采集的数据为:" + new Random().nextInt(10).toString
          store(message) //底层自动封装
          Thread.sleep(1000)

        }
      }
    }).start()
  }

  override def onStop(): Unit = {
    flag = false;
  }
}





























