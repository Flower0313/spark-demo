package com.atguigu.rdd.part

import org.apache.spark.Partitioner
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_part_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日21:00 - 周一
 * @Describe
 */
object rdd_part_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(List(
      ("nba", "xxxxx"),
      ("cba", "ccccc"),
      ("wnba", "wwwww"),
      ("nba", "bbbbbb"),
    ), 3)

    val partRdd: RDD[(String, String)] = rdd.partitionBy(new BasketPartitioner(3))

    partRdd.saveAsTextFile("output/output1")


    sc.stop()
  }

}

/*
* 自定义分区器
* 1.继承Partitioner
* 2.重写方法
* */
class BasketPartitioner(nums: Int) extends Partitioner {
  //分区数量
  override def numPartitions: Int = nums

  //返回数据的分区号
  override def getPartition(key: Any): Int = {
    key match {
      case "nbc" => 0
      case "cba" => 1
      case "wnba" => 2
      case _ => 2
    }
  }
}






































































