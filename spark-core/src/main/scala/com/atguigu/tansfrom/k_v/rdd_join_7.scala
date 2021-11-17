package com.atguigu.tansfrom.k_v
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_join_7 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日20:12 - 周日
 * @Describe join
 */
object rdd_join_7 {
  def main(args: Array[String]): Unit = {
    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc:SparkContext = new SparkContext(conf)

    val rdd1: RDD[(String, Int)] =
      sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3),("e",13),("a",313)))

    val rdd2: RDD[(String, Int)] =
      sc.makeRDD(List( ("b", 5), ("c", 6),("a", 4),("a",100)))

    //将相同的key连接在一起,如果两个数据源中没有匹配上则不会出现
    //所以只有两个数据源中共同的数据才会出现，相当于inner join，就是笛卡尔乘积，取有相同的部分
    //所以两个数据中有太多相同的谨慎使用
    rdd1.join(rdd2).collect().foreach(println)
    /*
    * (a,(1,4))
    * (a,(1,100))
    * (a,(313,4))
    * (a,(313,100))
    * (b,(2,5))
    * (c,(3,6))
    * */
    sc.stop()
  }
}
