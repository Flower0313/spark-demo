package com.atguigu.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @ClassName ScalaDemo-streaming_foreachRDD_13 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日21:09 - 周二
 * @Describe foreachRDD
 */
object streaming_foreachRDD_13 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lines = ssc.socketTextStream("hadoop102", 9999)


    val wordToOneDStream = lines.flatMap(_.split(" "))
      .map((_, 1))


    /*
    * 首先是作用范围不同，foreachRDD作用于DStream中每一个时间间隔的RDD，
    * foreachPartition 作用于每一个时间间隔的RDD中的每一个 partition，
    * foreach 作用于每一个时间间隔的 RDD 中的每一个元素。
    *
    * 总结：一个foreachRDD可能有多个RDD，一个RDD里面有多个分区，一个分区里面有多个元素
    * */
    //foreachRDD相当于拿到了最底层的RDD直接操作，它就不会出现时间戳了
    wordToOneDStream.foreachRDD(
      rdd => {
        println("2222:" + Thread.currentThread().getName)

        rdd.foreachPartition(
          iter => {
            iter.foreach(println)
            println("3333" + Thread.currentThread().getName)
          }
        )
      }
    )


    ssc.start()
    ssc.awaitTermination()
  }
}
