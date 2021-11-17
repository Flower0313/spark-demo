package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_groupBykey_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日9:44 - 周日
 * @Describe groupBykey
 */
object rdd_groupBykey_3 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc:SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3), ("b", 4)
    ))

    //将数据源中的数据相同key的分在一个组中，形成一个对偶元组
    //元组的第一个元素就是key,第二个元素相同key的value集合
    val groupBykeyRdd: RDD[(String, Iterable[Int])] = rdd.groupByKey()

    val groupByrDD: RDD[(String, Iterable[(String, Int)])] = rdd.groupBy(x => x._1)

    /*
    * groupBykey和groupBy的区别：
    * 1.前者固定是按key分组，且分组后会将key拿在独立外面显示，集合中没有key值
    * 2.后者只是将集合聚集到一个组
    *
    * groupBykey:(a,CompactBuffer(1, 2, 3))、(b,CompactBuffer(4))
    *
    * groupBy:(a,CompactBuffer((a,1), (a,2), (a,3)))、(b,CompactBuffer((b,4))
    *
    * */
    groupBykeyRdd.collect().foreach(println)
    groupByrDD.collect().foreach(println)

    sc.stop()
  }
}
