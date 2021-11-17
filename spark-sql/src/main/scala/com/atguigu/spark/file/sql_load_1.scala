package com.atguigu.spark.file

/**
 * @ClassName ScalaDemo-sql_load_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月30日21:57 - 周六
 * @Describe 文件的读取
 */
object sql_load_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.SparkConf
    import org.apache.spark.sql.{DataFrame, SparkSession}
    //todo 创建sparksql运行环境
    val conf: SparkConf = new SparkConf().setAppName("sparkSQL").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._ //这不是包，这是你创建spark对象的名称
    //todo 执行逻辑操作

    // 3.1 spark.read直接读取数据：csv   format   jdbc   json   load   option
    // options   orc   parquet   schema   table   text   textFile
    // 注意：加载数据的相关参数需写到上述方法中，
    // 如：textFile需传入加载数据的路径，jdbc需传入JDBC相关参数。
    spark.read.json("T:\\ShangGuiGu\\SparkDemo\\spark-sql\\input\\user.json").show()

    // 3.2 format指定加载数据类型
    // spark.read.format("…")[.option("…")].load("…")
    // format("…")：指定加载的数据类型，包括"csv"、"jdbc"、"json"、"orc"、"parquet"和"text"
    // load("…")：在"csv"、"jdbc"、"json"、"orc"、"parquet"和"text"格式下需要传入加载数据路径
    // option("…")：在"jdbc"格式下需要传入JDBC相应参数，url、user、password和dbtable
    spark.read.format("json").load ("input/user.json").show
    //或直接spark.read.json("")

    //todo 关闭环境
    spark.stop()
  }
}
