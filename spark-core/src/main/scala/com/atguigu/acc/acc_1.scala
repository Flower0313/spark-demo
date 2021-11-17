package com.atguigu.acc

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-acc_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日22:47 - 周一
 * @Describe
 */
object acc_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    /*val i: Int = rdd.reduce(_ + _)

    println(i)*/

    var sum: Int = 0
    rdd.foreach(
      i => {
        sum += i
      }
    )
    println(sum)
    /*
    * Q:为什么结果为0
    * A:Driver会将sum传到Executor中去，sum在E中已经被改变，
    *   但是不会返回给Sum，所以sum还是没变。
    * */

    sc.stop()
  }

}











































