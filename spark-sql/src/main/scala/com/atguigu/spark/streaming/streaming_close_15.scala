package com.atguigu.spark.streaming

import org.apache.commons.configuration.Configuration
import org.apache.commons.httpclient.URI
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}
import org.apache.spark.streaming.dstream.ReceiverInputDStream

import java.net.URI

/**
 * @ClassName ScalaDemo-streaming_close_15 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日23:19 - 周二
 * @Describe
 */
object streaming_close_15 {
  def main(args: Array[String]): Unit = {
    //1.初始化Spark配置信息
    val sparkconf = new SparkConf().setMaster("local[*]").setAppName("sparkStreaming")

    // 设置优雅的关闭
    sparkconf.set("spark.streaming.stopGracefullyOnShutdown", "true")

    //2.初始化SparkStreamingContext
    val ssc: StreamingContext = new StreamingContext(sparkconf, Seconds(3))

    // 接收数据
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)
    // 执行业务逻辑
    lineDStream.flatMap(_.split(" "))
      .map((_, 1))
      .print()

    // 开启监控程序
    new Thread(new MonitorStop(ssc)).start()

    //4 启动SparkStreamingContext
    ssc.start()

    // 将主线程阻塞，主线程不退出
    ssc.awaitTermination()

  }
}
// 监控程序
class MonitorStop(ssc: StreamingContext) extends Runnable{

  override def run(): Unit = {
    // 获取HDFS文件系统
    /*val fs: FileSystem = FileSystem.get(new URI("hdfs://hadoop102:8020"),
      new Configuration(), "holdenxiao");

    while (true){
      Thread.sleep(5000)
      // 获取/stopSpark路径是否存在
      val result: Boolean = fs.exists(new Path("hdfs://hadoop102:8020/stopSpark"))

      if (result){

        val state: StreamingContextState = ssc.getState()
        // 获取当前任务是否正在运行
        if (state == StreamingContextState.ACTIVE){
          // 优雅关闭
          ssc.stop(stopSparkContext = true, stopGracefully = true)
          System.exit(0)
        }
      }
    }*/
  }
}

