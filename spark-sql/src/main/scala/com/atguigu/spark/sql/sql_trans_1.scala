package com.atguigu.spark.sql

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-sql_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日18:51 - 周六
 * @Describe 添加log4j的ERROR级别后，重启一下idea,RDD、DS、DF三者之间的转换
 */
object sql_1 {
  def main(args: Array[String]): Unit = {
    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    //RDD和DF、DS转换必须要导的包(隐式转换),spark指的是上面的sparkSession
    import spark.implicits._ //这不是包，这是你创建spark对象的名称

    //todo 执行逻辑操作


    //todo DataFrame
    //DF只是特定泛型的DS
    //val df: DataFrame = spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json")
    //df.show()

    //todo DF => SQL
    //df.createOrReplaceTempView("user") //创建临时视图，能查不可改
    //spark.sql("select * from user").show

    //todo DF => DSL
    //df.select("age", "name").show

    //如果DF操作时涉及转换运算操作，需要转换规则
    //df.select($"age" + 1).show
    //df.select('age + 1).show

    //todo DataSet
    //val seq: Seq[Int] = Seq(1, 2, 3, 4)
    //val ds: Dataset[Int] = seq.toDS()
    //ds.show() //DF的方法DS都能用


    //todo RDD <=> DataFrame
    //以元组的格式
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List((1, "xiaohua", 18), (2, "flower", 22)))
    val df: DataFrame = rdd.toDF("id", "name", "age")
    //转回rdd
    val rowRdd: RDD[Row] = df.rdd

    //todo DataFrame <=> DataSet
    val ds: Dataset[User] = df.as[User]
    //DS再转回DF
    val df1: DataFrame = ds.toDF()

    //todo RDD <=> DataSet
    val ds2: Dataset[User] = rdd.map {
      case (id, name, age) => {
        User(id, name, age) //将rdd中每个List先转换为User
      }
    }.toDS()
    //DS再转回RDD
    val userRDD: RDD[User] = ds2.rdd

    //todo 关闭环境
    spark.stop()
  }
}

//在转DS时必须定义一个样例类来接收
case class User(val id: Int, val name: String, val age: Int)









































