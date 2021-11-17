package com.atguigu.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName ScalaDemo-sql_udf_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日19:44 - 周六
 * @Describe 用户自定义UDF函数
 */
object sql_udf_2 {
  def main(args: Array[String]): Unit = {
    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 执行逻辑操作
    val df: DataFrame = spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json")
    df.createOrReplaceTempView("user")

    //这里prefixName是用户自定义函数，用于给name的列名取别名
    spark.udf.register("prefixName", (name: String) => {
      "姓名:" + name
    })

    spark.sql("select prefixName(name),age from user").show

    /*
    * 打印结果：
    * |    姓名:xiaohua| 20|
      |     姓名:flower| 19|
      | 姓名:holdenxiao| 18|
    * */

    //todo 关闭环境
    spark.stop()
  }
}




































