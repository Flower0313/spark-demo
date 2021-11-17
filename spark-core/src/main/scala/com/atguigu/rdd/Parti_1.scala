package com.atguigu.rdd


/**
 * @ClassName ScalaDemo-Parti_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日11:40 - 周五
 * @Describe 从集合读取时指定分区
 */
object Parti_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}
    
    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")
    
    val sc:SparkContext = new SparkContext(conf)
    
    //todo 分区和并行度
    //四条数据分两个区，第二个参数不传递就会使用默认值：defaultParallelism(默认并行度)
    //默认值:scheduler.conf.getInt("spark.default.parallelism", totalCores),就是当前运行环境的最大可用核数
    val rdd = sc.makeRDD(
      List(1, 2, 3, 4)
    )
    //4个数据分到2个分区，【1、2】、【3、4】
    //4个数据分到5个分区，【】、【1】、【2】、【3】、【4】
    //4个数据分到3个分区，【1】、【2】、【3、4】
    //5个数据分到3个分区，【1】、【2、3】、【4、5】

    //将处理的数据保存成分区文件，这个输出文件不能存在
    rdd.saveAsTextFile("output/output2")





    sc.stop()
  }

}
