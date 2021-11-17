package com.atguigu.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable
import scala.collection.immutable

/**
 * @ClassName ScalaDemo-req_hotcategory_2_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日23:13 - 周二
 * @Describe
 */
object req_hotcategory_2_2 {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)

    //1 读取数据
    val lineRDD: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")
    //2 封装样例类 将lineRDD变为actionRDD
    val actionRDD: RDD[UserVisitAction] = lineRDD.map(
      line => {
        val datas: Array[String] = line.split("_")
        //将解析出来的数据封装到样例类里面
        UserVisitAction(
          datas(0),
          datas(1),
          datas(2),
          datas(3),
          datas(4),
          datas(5),
          datas(6),
          datas(7),
          datas(8),
          datas(9),
          datas(10),
          datas(11),
          datas(12)
        )
      }
    )
    //3 使用累加器统计相同品类id的点击数量,下单数量,支付数量
    //3.1 创建累加器
    val cateAcc = new CategoryCountAccumulator
    //3.2 注册累加器
    sc.register(cateAcc,"cateacc")
    //3.3 使用累加器
    actionRDD.foreach(action => cateAcc.add(action))
    //3.4 获得累加器的值 ((4,click),5961) ((4,order),1760) ((4,pay),1271)
    val accMap: mutable.Map[(String, String), Long] = cateAcc.value

    //4 将accMap按照品类id进行分组(4,Map((4,click) -> 5961, (4,order) -> 1760, (4,pay) -> 1271))
    val groupMap: Map[String, mutable.Map[(String, String), Long]] = accMap.groupBy(_._1._1)

    //5 将groupMap转换成样例类集合
    val infoIter: immutable.Iterable[CategoryCountInfo] = groupMap.map {
      case (id, map) => {
        val click = map.getOrElse((id, "click"), 0L)
        val order = map.getOrElse((id, "order"), 0L)
        val pay = map.getOrElse((id, "pay"), 0L)
        CategoryCountInfo(id, click, order, pay)
      }
    }
    //6 对样例类集合倒序排序取前10
    val result: List[CategoryCountInfo] = infoIter.toList.sortBy(info => (info.clickCount, info.orderCount, info.payCount))(Ordering[(Long, Long, Long)].reverse).take(10)
    result.foreach(println)

    //TODO 3 关闭资源
    sc.stop()
  }
}

//品类行为统计累加器
/**
 * 输入  UserVisitAction
 * 输出  ((鞋,click),1) ((鞋,order),1) ((鞋,pay),1)---mutable.Map[(String, String), Long]
 */

class CategoryCountAccumulator extends AccumulatorV2[UserVisitAction,mutable.Map[(String, String), Long]]{
  var map = mutable.Map[(String, String), Long]()

  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[UserVisitAction, mutable.Map[(String, String), Long]] = new CategoryCountAccumulator

  override def reset(): Unit = map.clear()

  override def add(action: UserVisitAction): Unit = {
    if(action.click_category_id != "-1"){
      //点击  key=(cid,"click")
      val key = (action.click_category_id,"click")
      map(key) = map.getOrElse(key,0L) + 1L
    }else if(action.order_category_ids != "null"){
      //下单  key=(cid,"order")
      val cids: Array[String] = action.order_category_ids.split(",")
      for (cid <- cids) {
        val key = (cid,"order")
        map(key) = map.getOrElse(key,0L) + 1L
      }

    }else if(action.pay_category_ids != "null"){
      //支付  key=(cid,"pay")
      val cids: Array[String] = action.pay_category_ids.split(",")
      for (cid <- cids) {
        val key = (cid,"pay")
        map(key) = map.getOrElse(key,0L) + 1L
      }
    }
  }

  override def merge(other: AccumulatorV2[UserVisitAction, mutable.Map[(String, String), Long]]): Unit = {
    other.value.foreach {
      case (key, count) => {
        map(key) = map.getOrElse(key, 0L) + count
      }
    }
  }

  override def value: mutable.Map[(String, String), Long] = map
}
