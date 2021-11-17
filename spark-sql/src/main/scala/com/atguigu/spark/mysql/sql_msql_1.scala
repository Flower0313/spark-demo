package com.atguigu.spark.mysql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SaveMode}

/**
 * @ClassName ScalaDemo-sql_msql_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日22:34 - 周六
 * @Describe 访问mysql
 */
object sql_msql_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.SparkConf
    import org.apache.spark.sql.{DataFrame, SparkSession}
    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 读取数据
    val df: DataFrame = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/gmall") //数据库名
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "root")
      .option("dbtable", "spark_mysql") //表名
      .load()

    //todo 创建视图
    //df.createOrReplaceTempView("user")

    //todo 查询数据
    //spark.sql("select id,name from user").show()
    //df.show

    //todo 写数据
    //准备数据,注意主键不能重复
    val rdd: RDD[User] = spark.sparkContext.makeRDD(List(User(13, "holden", 68), User(31, "zzx", 99)))

    val ds: Dataset[User] = rdd.toDS()


    ds.write
      .format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/gmall") //数据库名
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "root")
      .option("dbtable", "spark_mysql") //表名
      .mode(SaveMode.Append)
      .save()

    //todo 关闭环境
    spark.stop()
  }
}

case class User(id: Int, name: String, age: Int)
