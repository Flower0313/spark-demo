package com.atguigu.req

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-req_seesion_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日23:21 - 周二
 * @Describe Top10热门品类中每个品类的Top10活跃Session统计
 */
object req_session_1 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    //1.读数据
    val rdd: RDD[String] = sc.textFile("datas/source/user_visit_action.txt")
    rdd.cache()
    //广播变量，获取前10热门中的id
    val ids: Broadcast[Array[String]] = sc.broadcast(top10Category(rdd))

    //1.过滤原始数据，保留点击前10和品类ID
    val filterRdd: RDD[String] = rdd.filter {
      t => {
        //判断用户是否不等于-1且是否在前10的数据中
        val datas: Array[String] = t.split("_")
        (datas(6) != "-1" && ids.value.contains(datas(6)))
      }
    }

    //2.根据品类ID和session来进行点击量的统计
    val reduceRdd: RDD[((String, String), Int)] = filterRdd.map {
      t => {
        val datas: Array[String] = t.split("_")
        //((品类id,用户session),1)
        ((datas(6), datas(2)), 1)
      }
    }.reduceByKey(_ + _) //key是(品类ID,用户session)

    //3.将统计结果进行结构转换 ((品类id,sessionId),sum) => (品类id,(sessionId,sum))
    val mapRdd: RDD[(String, (String, Int))] = reduceRdd.map {
      case ((cid, sid), sum) => (cid, (sid, sum))
    }

    //4.相同品类的进行分组
    val groupRdd: RDD[(String, Iterable[(String, Int)])] = mapRdd.groupByKey()

    //5.将分组后的数据进行点击量排序，取top10
    val sortRdd: RDD[(String, List[(String, Int)])] = groupRdd.mapValues {
      i => {
        i.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
      }
    }

    sortRdd.collect().foreach(println)


    sc.stop()
  }

  def top10Category(rdd: RDD[String]) = {
    //先统一数据结构，这样reduceByKey会先在分区内相同key的合并，就不会分区间去合并了，减少shuffle量
    val resRdd = rdd.flatMap {
      x => {
        val datas: Array[String] = x.split("_")
        if (datas(6) != "-1") {
          //点击量
          Array((datas(6), (1, 0, 0))) //flatMap返回的就是集合,才能被扁平化
        } else if (datas(8) != "null") {
          //下单量
          val chips: Array[String] = datas(8).split(",")
          chips.map(x => (x, (0, 1, 0)))
        } else if (datas(10) != "null") {
          //支付量
          val chips: Array[String] = datas(10).split(",")
          chips.map((_, (0, 0, 1)))
        } else {
          Nil
        }
      }
    }.reduceByKey((u1, u2) => {
      (u1._1 + u2._1, u1._2 + u2._2, u1._3 + u2._3)
    }).sortBy(x => x._2, ascending = false).take(10).map(_._1) //因为比的是三元组，所以会调用Ordering的Tuples3方法

    resRdd
  }
}
