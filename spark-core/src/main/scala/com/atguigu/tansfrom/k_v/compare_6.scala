package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-compare 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日19:11 - 周日
 * @Describe
 */
object compare_6 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ), 2)

    /*
    * reduceByKey():
    *    ->combineByKeyWithClassTag[V](
    *         (v: V) => v,//第一个值不会参与计算
    *         func, //分区内计算规则
    *         func)  //分区间计算规则
    *
    * aggregateByKey():
    *    ->combineByKeyWithClassTag[U](
    *         (v: V) => cleanedSeqOp(createZero(), v),//初始值和第一个key的value值进行的操作
    *         cleanedSeqOp,
    *         combOp)
    *
    * foldByKey():
    *    ->combineByKeyWithClassTag[V](
    *         (v: V) => cleanedFunc(createZero(), v),//初始值和第一个key的value值进行的操作
    *         cleanedFunc,
    *         cleanedFunc)
    *
    * combineByKey():
    *    ->combineByKeyWithClassTag(
    *         createCombiner, //相同key的第一条数据进行处理
    *         mergeValue, //分区内数据处理函数
    *         mergeCombiners  //分区间数据处理函数
    *         )(null)

    *
    * */

    //四种wordCount
    rdd.reduceByKey(_ + _)
    rdd.aggregateByKey(0)(_ + _, _ + _)
    rdd.foldByKey(0)(_ + _)
    rdd.combineByKey(v => v, (x: Int, y) => x + y, (x: Int, y: Int) => x + y)

    sc.stop()

  }

}


































