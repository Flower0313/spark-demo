package com.atguigu.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

/**
 * @ClassName ScalaDemo-req_hotcategory_4 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日20:36 - 周二
 * @Describe 方式一3.0 -- 除去shuffle的操作，使用累加器
 */
object req_hotcategory_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //1.读数据
    val rdd: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")

    val acc: HotCategoryAccumulator = new HotCategoryAccumulator
    sc.register(acc, "hot_category")

    rdd.foreach {
      x => {
        val datas: Array[String] = x.split("_")
        if (datas(6) != "-1") {
          //点击量
          acc.add((datas(6), "click"))
        } else if (datas(8) != "null") {
          //下单量
          val chips: Array[String] = datas(8).split(",")
          chips.foreach(x => {
            acc.add(x, "order")
          })
        } else if (datas(10) != "null") {
          //支付量
          val chips: Array[String] = datas(10).split(",")
          chips.foreach(x => acc.add(x, "pay"))
        }
      }
    }

    val resRDD: Iterable[HotCategory] = acc.value.values


    val result: Seq[HotCategory] = resRDD.toList.sortBy(info =>
      (info.clickCnt.toLong, info.orderCnt.toLong, info.payCnt.toLong))(Ordering[(Long, Long, Long)].reverse)
      .take(10)

    println(result)
    Thread.sleep(Int.MaxValue)
    sc.stop()
  }

  //自定义一个类,构造参数添加var就自动变成属性了
  case class HotCategory(cid: String, var clickCnt: Int, var orderCnt: Int, var payCnt: Int)

  /*
  * 自定义累加器
  * IN:(品类,行为类型)
  * OUT:mutable.Map[String,HotCategory]
  * */
  class HotCategoryAccumulator extends AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] {

    //需要返回给Driver的集合，而且这个变量要计算累加和
    private var hcMap = mutable.Map[String, HotCategory]()

    override def isZero: Boolean = {
      hcMap.isEmpty
    }

    override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] = {
      new HotCategoryAccumulator()
    }

    override def reset(): Unit = {
      hcMap.clear()
    }

    override def add(v: (String, String)): Unit = {
      val cid: String = v._1
      val actionType: String = v._2

      //如果你从没出现过，就赋一个新的值,有就返回cid对应的HotCategory
      val category: HotCategory = hcMap.getOrElse(cid, HotCategory(cid, 0, 0, 0))

      if (actionType == "click") {
        category.clickCnt += 1
      } else if (actionType == "order") {
        category.orderCnt += 1
      } else if (actionType == "pay") {
        category.payCnt += 1

      }
      hcMap.update(cid, category)
    }

    //在Driver中合并来自各个不同Executor的数据
    override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]): Unit = {
      val map1: mutable.Map[String, HotCategory] = this.hcMap
      val map2: mutable.Map[String, HotCategory] = other.value

      map2.foreach {
        case (cid, hc) => {
          //如果map1中没有map2中有的数据,那就创建一个新的，然后再加上map2中的数据
          val category: HotCategory = map1.getOrElse(cid, HotCategory(cid, 0, 0, 0))
          category.clickCnt += hc.clickCnt
          category.orderCnt += hc.orderCnt
          category.payCnt += hc.payCnt
          //更新
          map1.update(cid, category)
        }
      }

    }

    override def value: mutable.Map[String, HotCategory] = {
      hcMap
    }
  }

}







































