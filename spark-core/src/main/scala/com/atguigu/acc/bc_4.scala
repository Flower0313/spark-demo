package com.atguigu.acc

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

import scala.collection.mutable

/**
 * @ClassName ScalaDemo-bc_4 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日9:18 - 周二
 * @Describe 广播变量
 */
object bc_4 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("c", 3)
    ))

    /*val rdd2: RDD[(String, Long)] = sc.makeRDD(List(
      ("a", 4), ("b", 5), ("c", 6)
    ))*/

    val map = mutable.Map(("a", 4), ("b", 5), ("c", 6))

    //广播变量
    val bc: Broadcast[mutable.Map[String, Int]] = sc.broadcast(map)


    //有特殊的处理方式代替了join
    val mapRdd: RDD[(String, Any)] = rdd1.map {
      case (k, v) => {
        //访问广播变量
        (k, bc.value.getOrElse(k, 0) + v)
      }
    }

    mapRdd.collect().foreach(println)



    /*val joinRdd: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    joinRdd.foreach(println)*/
    Thread.sleep(Int.MaxValue)
    sc.stop()
  }
}







































