package com.atguigu.spark.streaming

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

/**
 * @ClassName ScalaDemo-streaming_reduceByKeyAndWindow_12 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月02日20:55 - 周二
 * @Describe
 */
object streaming_reduceByKeyAndWindow_12 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")

    val ssc = new StreamingContext(sparkConf, Seconds(3))

    ssc.checkpoint("output/cp1")

    val lines = ssc.socketTextStream("hadoop102", 9999)

    val wordToOne: DStream[(String, Int)] = lines.flatMap(_.split(" "))
      .map((_, 1))

    /*
    * 但这种情况如果你后续不再有数据输入，会一直显示以前重复的元素，且个数都是0，这些元素明明没输入了
    * 为什么还能还看见呢？因为已经将它们存在了checkpoint中，没有加新元素和减老元素都是根据checkpoint
    * 中记录的值来获取的。
    * (a,0)
    * (b,0)
    * */
    wordToOne.reduceByKeyAndWindow(
      (a: Int, b: Int) => (a + b),
      (x: Int, y: Int) => (x - y),
      Seconds(9),
      Seconds(6)
    ).print()

    //上述情况的解决办法：
    wordToOne.reduceByKeyAndWindow(
      (a: Int, b: Int) => (a + b),
      (x: Int, y: Int) => (x - y),
      Seconds(12),
      Seconds(6),
      new HashPartitioner(2),//至于为什么加它呢？因为没有这几个参数的组合，所以要加个打酱油的凑数
      (x:(String, Int)) => x._2 > 0
    ).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
