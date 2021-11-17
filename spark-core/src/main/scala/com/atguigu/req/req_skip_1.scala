package com.atguigu.req

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-req_skip_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日10:25 - 周三
 * @Describe 全页面跳转统计
 */
object req_skip_1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    //1.读数据
    val rdd: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")

    //数据映射样例类
    val actionRDD: RDD[UserVisitAction] = rdd.map {
      action => {
        val datas: Array[String] = action.split("_")
        UserVisitAction(
          datas(0), //日期
          datas(1), //用户id
          datas(2), //session_id
          datas(3), //页面id
          datas(4), //时间戳
          datas(5), //搜索关键字
          datas(6), //点击品类id
          datas(7), //商品id
          datas(8), //下单品类集合
          datas(9),
          datas(10),
          datas(11),
          datas(12), //城市id
        )
      }
    }

    //缓存数据
    //actionRDD.cache()


    //todo 计算分母 拉取到Driver中形成独立的结果
    val pageIdToCount: Map[String, Int] = actionRDD.map {
      x => (x.page_id, 1)
    }.reduceByKey(_ + _).collect().toMap


    //todo 计算分子
    //相同的用户放在同一组中
    val userGroup: RDD[(String, Iterable[UserVisitAction])] = actionRDD.groupBy(x => x.session_id)

    //分组后根据时间进行排序((页面1,页面2),1)
    val mapRdd: RDD[(String, List[((String, String), Int)])] = userGroup.mapValues {
      t => {
        //对每组的时间进行升序排序
        val sortList: List[UserVisitAction] = t.toList.sortBy(x => x.action_time)
        //只需页面id即可，冗余的数据可以删除
        val flowIds: List[String] = sortList.map(_.page_id)
        //将顺序的页面id转换成理想格式:【1,2,3,4】=>【1-2,2-3,3-4】,使用滑窗或拉链
        val pageFlowIds: List[(String, String)] = flowIds.zip(flowIds.tail)
        pageFlowIds.map(
          t => (t, 1)
        )
      }
    }
    //将元组集合打散成单个元组
    val sumRdd: RDD[((String, String), Int)] = mapRdd.values //保留后面的值
      .flatMap(x => x) //将每个List打散

    //分子的数据和((页面1,页面2),sum)
    val reduceRdd: RDD[((String, String), Int)] = sumRdd.reduceByKey(_ + _)


    //todo 计算单跳转换率

    val resRdd: Array[((String, String), Int)] = reduceRdd.collect()

    resRdd.foreach {
      case ((page1, page2), sum) => {
        //从上面的分母中取具体的分母
        val y: Long = pageIdToCount.getOrElse(page1, 0).toLong
        println(s"页面${page1}的跳到${page2}的转换率为:" + sum.toDouble / y)
      }
    }
    sc.stop()
  }
}












































