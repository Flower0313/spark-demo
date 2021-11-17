package com.atguigu.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

/**
 * @ClassName ScalaDemo-acc_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日22:47 - 周一
 * @Describe 累加器做法
 */
object acc_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //声明系统自带累加器
    val sumAcc: LongAccumulator = sc.longAccumulator("sum")

    var sum: Int = 0
    rdd.foreach(
      i => {
        //使用累加器
        sumAcc.add(i)
      }
    )
    //获取累加器的值
    println(sumAcc.value)//10
    sc.stop()
    /*
    * executor不建议去读，因为不正确
    * */
  }

}

//累加器少加情况
object acc_2_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //声明系统自带累加器
    val sumAcc: LongAccumulator = sc.longAccumulator("sum")


    var sum: Int = 0
    rdd.map(num => {
      sumAcc.add(num)
      num
    })
    println(sum)//0
    //少加:转换算子中调用累加器,如果没有行动算子的话，那么不会执行
    sc.stop()
  }

}

//累加器多加情况
object acc_2_3 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //声明系统自带累加器
    val sumAcc: LongAccumulator = sc.longAccumulator("sum")


    var sum: Int = 0
    val mapRdd: RDD[Int] = rdd.map(num => {
      sumAcc.add(num)
      num
    })
    //多加:多次执行行动算子会执行多次
    mapRdd.collect()
    mapRdd.collect()
    println(sum)//20
    sc.stop()
  }

}








































