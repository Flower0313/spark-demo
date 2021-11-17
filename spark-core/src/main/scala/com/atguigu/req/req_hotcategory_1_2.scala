package com.atguigu.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-req_hotcategory_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日16:47 - 周二
 * @Describe 方式一2.0,使用union
 */
object req_hotcategory_1_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //1.读数据
    val rdd: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")

    //改进一：缓存后面需要多次读的RDD
    rdd.cache()

    //2.统计品类的点击数量(品类ID,点击数量)
    val clickCountRdd: RDD[(String, Int)] = rdd
      .filter(_.split("_")(6) != "-1") //过滤没有点击的数据
      .map {
        x => {
          (x.split("_")(6), 1)
        }
      }.reduceByKey(_ + _) //将相同类的商品点击量进行聚合(品类id,点击数)

    //3.统计品类的下单数量(品类ID,下单数量)
    val orderCountRdd: RDD[(String, Int)] = rdd.filter(_.split("_")(8) != "null") // 过滤
      .flatMap {
        x => {
          val datas: Array[String] = x.split("_") //根据_拆分
          val chip: Array[String] = datas(8).split(",") //获取到订单集合以,号分割
          chip.map(x => (x, 1)) //将每个单个集合元素转换成元组集合
        }
      }.reduceByKey(_ + _)

    //4.统计品类的支付数量 (品类ID,支付数量)
    val payCountRdd: RDD[(String, Int)] = rdd.filter(_.split("_")(10) != "null")
      .flatMap(
        x => {
          val datas: String = x.split("_")(10)
          val chips: Array[String] = datas.split(",")
          chips.map(x => (x, 1))
        }
      ).reduceByKey(_ + _)

    //5.对数据进行改造，改成(品类id,(点击量,订单量,支付量))的格式,【1,(21,0,0)】、【1,(0,34,0)】、【1,(0,0,11)】
    val clickRDD: RDD[(String, (Int, Int, Int))] = clickCountRdd.map { x => (x._1, (x._2, 0, 0)) }
    val orderRDD: RDD[(String, (Int, Int, Int))] = orderCountRdd.map { x => (x._1, (0, x._2, 0)) }
    val payRDD: RDD[(String, (Int, Int, Int))] = payCountRdd.map { x => (x._1, (0, 0, x._2)) }

    //【1,(21,0,0)】、【1,(0,34,0)】、【1,(0,0,11)】 => 【1,(21,34,11)】
    val unionRDD: RDD[(String, (Int, Int, Int))] = clickRDD.union(orderRDD).union(payRDD)
    val resRDD: Array[(String, (Int, Int, Int))] = unionRDD.reduceByKey {
      (u1, u2) => { //u1 u2表示两个相邻的(点击量,订单量,支付量)
        (u1._1 + u2._1, u1._2 + u2._2, u2._3 + u2._3)//两两求和
      }
    }.sortBy(x => x._2,false).take(10)

    resRDD.foreach(println)
    Thread.sleep(Int.MaxValue)

    sc.stop()
  }
}
