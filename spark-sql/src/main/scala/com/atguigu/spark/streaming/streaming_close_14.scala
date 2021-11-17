package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

/**
 * @ClassName ScalaDemo-streaming_close_14 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日22:47 - 周二
 * @Describe
 */
class streaming_close_14 {
  def main(args: Array[String]): Unit = {
    /*val thread = new Thread()
    thread.start()

    thread.stop()//强制关闭不可取*/
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.socketTextStream("hadoop102", 9999)

    lines.print()


    ssc.start()

    //如果想要关闭采集器，就要创建新的线程和主线程独立开,而且需要在第三方增加关闭状态
    new Thread(new Runnable {
      override def run(): Unit = {
        //优雅的关闭:不是强制关闭，先让计算节点不再接收新的数据，然后再让当前结点处理完毕后再关闭
        while (true) {
          //这个if中应该是第三方来判断，可我们这没有，就用的ture模拟
          if (true) {
            //获取SparkStreaming状态,判断状态，只有开启状态才需要关闭
            val state: StreamingContextState = ssc.getState()
            if (state == StreamingContextState.ACTIVE) {
              ssc.stop(true, true)
            }
          }
          Thread.sleep(5000)//隔5秒判断一次
        }
      }
    })

    ssc.awaitTermination() //阻塞main线程,那写在它下面的代码执行不到


  }
}
