package com.atguigu.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


/**
 * @ClassName ScalaDemo-req_hotcategory_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日11:05 - 周二
 * @Describe 热门品类Top10:方式一1.0
 */
object req_hotcategory_1 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //1.读数据
    val rdd: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")

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

    //5.进行全外连接,因为有点击量的可能并没有买
    val cogroupRdd: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] =
      clickCountRdd.cogroup(orderCountRdd, payCountRdd)

    val allCountRdd: RDD[(String, (Int, Int, Int))] = cogroupRdd.mapValues {
      case (iter1, iter2, iter3) => {
        //统计点击量的和
        var clickCnt: Int = 0
        val clickIter: Iterator[Int] = iter1.iterator
        //为什么不循环相加,因为之前已经reduceByKey好了,所以这里面只有一个元素
        if (clickIter.hasNext) {
          clickCnt = clickIter.next()
        }


        //统计订单的和
        var orderCnt: Int = 0
        val orderIter: Iterator[Int] = iter2.iterator
        if (orderIter.hasNext) {
          orderCnt = orderIter.next()
        }

        //统计支付的和
        var payCnt: Int = 0
        val payIter: Iterator[Int] = iter3.iterator
        if (payIter.hasNext) {
          payCnt = payIter.next()
        }
        (clickCnt, orderCnt, payCnt)//(商品id,(点击量,生成订单量,支付量))
      }
    }


    //sortBy比对元组的时候(a,b,c)会先比对a的值，相同再比较b的值，以此类推到最后一次
    //这里sortBy的底层会调用sortByKey,而sortByKey又会调用collect()或save,所以也会产生一个job
    val res: Array[(String, (Int, Int, Int))] =
      allCountRdd.sortBy(x => x._2, false).take(10)

    res.foreach(println)

    Thread.sleep(Int.MaxValue)
    sc.stop()
  }
}
