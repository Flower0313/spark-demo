package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets

/**
 * @ClassName ScalaDemo-streaming_diy2_4 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月31日17:15 - 周日
 * @Describe 自定义数据源，监控某端口
 */
object streaming_diy2_4 {
  def main(args: Array[String]): Unit = {
    //1.初始化Spark配置信息
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    //2.初始化SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //3.创建自定义receiver的Streaming
    val lineDStream = ssc.receiverStream(new CustomerReceiver("hadoop102", 9999))

    //4.将每一行数据做切分，形成一个个单词
    val wordDStream = lineDStream.flatMap(_.split(" "))

    //5.将单词映射成元组（word,1）
    val wordToOneDStream = wordDStream.map((_, 1))

    //6.将相同的单词次数做统计
    val wordToSumDStream = wordToOneDStream.reduceByKey(_ + _)

    //7.打印
    wordToSumDStream.print()

    //8.启动SparkStreamingContext
    ssc.start()
    ssc.awaitTermination()

  }
}

/**
* @param host ： 主机名称
* @param port ： 端口号
*  Receiver[String] ：返回值类型：String
*  StorageLevel.MEMORY_ONLY： 返回值存储级别
*/
class CustomerReceiver(host: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_ONLY) {
  // receiver刚启动的时候，调用该方法，作用为：读数据并将数据发送给Spark
  override def onStart(): Unit = {
    //在onStart方法里面创建一个线程,专门用来接收数据
    new Thread("Socket Receiver") {
      override def run() {
        receive()
      }
    }.start()
  }

  // 读数据并将数据发送给Spark
  def receive(): Unit = {

    // 创建一个Socket
    var socket: Socket = new Socket(host, port)

    // 字节流读取数据不方便,转换成字符流buffer,方便整行读取
    val reader = new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8))

    // 读取数据
    var input: String = reader.readLine()

    //当receiver没有关闭并且输入数据不为空，就循环发送数据给Spark
    while (!isStopped() && input != null) {
      store(input)
      input = reader.readLine()
    }

    // 如果循环结束，则关闭资源
    reader.close()
    socket.close()

    //重启接收任务
    restart("restart")
  }

  override def onStop(): Unit = {}
}

