package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-streaming_kafka_5 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月01日23:11 - 周一
 * @Describe 有状态的采集器（使批次数据汇总）updateStateByKey
 */
object streaming_state_6 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    //2.初始化SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    ssc.checkpoint("output/cp")//更新的数据会保存在这里

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)

    val wordToOne: DStream[(String, Int)] = lines.map((_, 1))

    //根据key对数据的状态进行更新
    //参数一：相同key 的value数据  参数二：缓冲区相同key的value数据,因为缓冲区有可能没值，所以用Option
    //seq.sum是当前批次的(k,sum)，然后buff.getOrElse(0)会找到是否有相同key的值，你只需要取出来就行
    val state: DStream[(String, Int)] = wordToOne.updateStateByKey(
      (seq: Seq[Int], buff: Option[Int]) => {
        val newCount: Int = buff.getOrElse(0) + seq.sum //buff有值就取出来，没有就默认为0
        Option(newCount)
      }
    )
    state.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
