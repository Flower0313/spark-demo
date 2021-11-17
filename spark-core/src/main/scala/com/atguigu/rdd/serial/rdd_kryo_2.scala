package com.atguigu.rdd.serial
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_kryo_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日14:25 - 周一
 * @Describe kryo
 */
object rdd_kryo_2 {
  def main(args: Array[String]): Unit = {

    val conf:SparkConf = new SparkConf()
      .setAppName("rdd")
      .setMaster("local[*]")
      // 替换默认的序列化机制
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      // 注册需要使用kryo序列化的自定义类,可以注册多个类
      .registerKryoClasses(Array(classOf[Search2]))

    val sc:SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(Array("hello world", "hello atguigu", "atguigu", "hahah"), 2)

    val search = new Search2("hello")
    val result: RDD[String] = search.getMatchedRDD1(rdd)

    result.collect.foreach(println)
    sc.stop()
  }
}
class Search2(val query: String) extends Serializable{

  def isMatch(s: String) = {
    s.contains(query)
  }

  def getMatchedRDD1(rdd: RDD[String]) = {
    rdd.filter(isMatch)
  }

  def getMatchedRDD2(rdd: RDD[String]) = {
    rdd.filter(_.contains(this.query))
  }
}

