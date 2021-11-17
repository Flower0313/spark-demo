package com.atguigu.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-req_hotcategory_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日22:53 - 周二
 * @Describe 方式二 -- 样例类
 */
object req_hotcategory_3 {
  def main(args: Array[String]): Unit = {
    //TODO 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)

    //1 读取数据
    val lineRDD: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")
    //2 封装样例类 将lineRDD变为actionRDD
    val actionRDD: RDD[UserVisitAction] = lineRDD.map(
      line => {
        val datas: Array[String] = line.split("_")
        //将解析出来的数据封装到样例类里面
        UserVisitAction( //将属性一一对应进去
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
    //3 转换数据结构 将actionRDD的数据变为CategoryCountInfo(品类ID,1,0,0)
    val infoRDD: RDD[(String, CategoryCountInfo)] = actionRDD.flatMap(
      action => {
        if (action.click_category_id != "-1") {
          //点击的数据
          List((action.click_category_id, CategoryCountInfo(action.click_category_id, 1, 0, 0)))
        } else if (action.order_category_ids != "null") {
          //下单的数据
          val arr: Array[String] = action.order_category_ids.split(",")
          arr.map(id => (id, CategoryCountInfo(id, 0, 1, 0)))
        } else if (action.pay_category_ids != "null") {
          //支付的数据
          val arr: Array[String] = action.pay_category_ids.split(",")
          arr.map(id => (id, CategoryCountInfo(id, 0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    /*
    * (1,CategoryCountInfo(1,1,0,0))
    * (18,CategoryCountInfo(18,1,0,0))
    * */

    // 4 按照品类id分组,两两聚合
    val reduceRDD: RDD[CategoryCountInfo] = infoRDD.reduceByKey(
      (info1, info2) => {
        info1.orderCount += info2.orderCount
        info1.clickCount += info2.clickCount
        info1.payCount += info2.payCount
        info1
      }
    ).map(_._2) //只取元组的第二个元素，因为第一个元素是id，第二个元素中已经包含了

    //6 将聚合后的数据 倒序排序 取前10
    val result: Array[CategoryCountInfo] = reduceRDD.sortBy(info => (info.clickCount, info.orderCount, info.payCount), false).take(10)
    result.foreach(println)
    Thread.sleep(Int.MaxValue)

    //TODO 关闭资源
    sc.stop()
  }
}




