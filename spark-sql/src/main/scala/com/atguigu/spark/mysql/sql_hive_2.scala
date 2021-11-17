package com.atguigu.spark.mysql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @ClassName ScalaDemo-sql_hive_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日22:44 - 周六
 * @Describe 访问hive
 */
object sql_hive_2 {
  def main(args: Array[String]): Unit = {

    //将hadoop用户名改成有权限的用户
    //System.setProperty("HADOOP_USER_NAME","holden")
    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

    //todo 连接外部hive
    //1.将虚拟机上的hive-site.xml拷贝下来
    //2.在虚拟机上启动hiveserver2或启动hive(启动hive前必须启动hadoop)
    //spark.sql("show databases").show()
    spark.sql("use atguigu")
    //spark.sql("select * from city_info limit 5").show()

    spark.udf.register("cityRemark", functions.udaf(new CityRemarkUDAF()))
    spark.sql(
      """
        |select * from (
        |select
        |    area,product_name,count(click_product_id),
        |       rank() over(partition by area order by count(1) desc) rk,
        |       cityRemark(city_name) as city_remark -- 这是一组一组调用，因为我们groupBy了,和窗口函数一起用会有点问题，最后先分组，统计出结果后再使用窗口函数
        |from user_visit_action u
        |    left join city_info c on u.city_id=c.city_id
        |    left join product_info p on u.click_product_id=p.product_id
        |where click_product_id>-1
        |group by area,product_name order by 3 desc) t1
        |where t1.rk<=3
        |""".stripMargin).show()

    //todo 关闭环境
    spark.stop()
  }
}

//中间缓存数据格式
case class Buffer(var totalCnt: Long, var cityMap: mutable.Map[String, Long])

/*
* IN:城市名称 String类型
* BUFF:totalCnt 总点击量，Map[(cityName,点击量)]类似(199,Map(('北京',99),('上海',100)))
* OUT:城市备注 String
* */

class CityRemarkUDAF extends Aggregator[String, Buffer, String] {
  //缓冲区初始化
  override def zero: Buffer = Buffer(0L, mutable.Map[String, Long]())

  //更新缓冲区
  override def reduce(buff: Buffer, city: String): Buffer = {
    //一个城市过来后，总数量+1
    buff.totalCnt += 1
    //这个城市的单独统计的数量也加1，若第一次没有就初始值为0
    val newCount: Long = buff.cityMap.getOrElse(city, 0L) + 1
    //更新缓冲区
    buff.cityMap.update(city, newCount)
    buff
  }

  //合并缓冲区
  override def merge(b1: Buffer, b2: Buffer): Buffer = {
    b1.totalCnt = b1.totalCnt + b2.totalCnt

    //合并两个map,循环的是map1,初始值的map2
    b1.cityMap = b2.cityMap.foldLeft(b1.cityMap) {
      case (b1, (city, cnt)) => {
        b1(city) = b1.getOrElse(city, 0L) + cnt //b1中如果有就修改，没有就添加
        b1
      }
    }
    b1

    /*val map1 = b1.cityMap
    val map2 = b2.cityMap
    b1.cityMap = map1.foldLeft(map2) {
      case (map, (city, cnt)) => {
        val newCount = map.getOrElse(city, 0L) + cnt
        map.update(city, newCount)
        map
      }
    }
    b1*/
  }

  //将统计的结果生成字符串信息,也就是生成最后的城市备注
  override def finish(buff: Buffer): String = {
    //定义一个可变List
    val remarkList = ListBuffer[String]()

    val totalCnt = buff.totalCnt
    val cityMap = buff.cityMap

    //排序
    val cityCntList: List[(String, Long)] =
      cityMap.toList.sortWith(_._2 > _._2).take(2)


    var sum: Long = 0L

    // 计算出前两名的百分比
    cityCntList.foreach {
      case (city, cnt) => {
        val r = cnt * 100 / buff.totalCnt
        remarkList.append(city + " " + r + "%")
        sum += r
      }
    }

    // 如果城市个数大于2，用其他表示
    if (cityMap.size > 2) {
      remarkList.append("其他 " + (100 - sum) + "%")
    }

    remarkList.mkString(",") //以逗号分割
  }

  override def bufferEncoder: Encoder[Buffer] = Encoders.product

  override def outputEncoder: Encoder[String] = Encoders.STRING
}


































