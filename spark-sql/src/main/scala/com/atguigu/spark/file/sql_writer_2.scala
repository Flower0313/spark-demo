package com.atguigu.spark.file

/**
 * @ClassName ScalaDemo-sql_writer_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日22:08 - 周六
 * @Describe 文件保存
 */
class sql_writer_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.SparkConf
    import org.apache.spark.sql.{DataFrame, SparkSession}
    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 执行逻辑操作
    val df: DataFrame = spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json")


    // 4.1 df.write.保存数据：csv  jdbc   json  orc   parquet  text
    // 注意：保存数据的相关参数需写到上述方法中。如：text需传入加载数据的路径，JDBC需传入JDBC相关参数。
    // 默认保存为parquet文件（可以修改conf.set("spark.sql.sources.default","json")）
    df.write.save("output")

    // 默认读取文件parquet
    spark.read.load("output").show()

    // 4.2 format指定保存数据类型
    // df.write.format("…")[.option("…")].save("…")
    // format("…")：指定保存的数据类型，包括"csv"、"jdbc"、"json"、"orc"、"parquet"和"text"。
    // save ("…")：在"csv"、"orc"、"parquet"和"text"(单列DF)格式下需要传入保存数据的路径。
    // option("…")：在"jdbc"格式下需要传入JDBC相应参数，url、user、password和dbtable
    df.write.format("json").save("output2")

    // 4.3 可以指定为保存格式，直接保存，不需要再调用save了
    df.write.json("output1")

    // 4.4 如果文件已经存在则追加
    df.write.mode("append").json("output2")

    // 如果文件已经存在则忽略(文件存在不报错,也不执行;文件不存在,创建文件)
    df.write.mode("ignore").json("output2")

    // 如果文件已经存在则覆盖,删除原有的文件夹，重新生成数据
    df.write.mode("overwrite").json("output2")

    // 默认default:如果文件已经存在则抛出异常
    // path file:/E:/ideaProject2/SparkSQLTest/output2 already exists.;
    df.write.mode("error").json("output2")


    //todo 关闭环境
    spark.stop()
  }
}
