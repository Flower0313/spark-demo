package com.atguigu.rdd

/**
 * @ClassName ScalaDemo-Parti_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日16:29 - 周五
 * @Describe 从文件读取时指定分区
 */
object Parti_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    // 分区默认值minPartitions = math.min(defaultParallelism, 2):最小分区数量
    // 又defaultParallelism在local模式下是8，取最小那就是2个分区
    // 【hello hello】、【world】
    //val rdd = sc.textFile("datas/2.txt")

    //指定最小分区数，注意这里不是你填几个分区就生成几个分区
    //分区计算方式：读取文件底层使用的是Hadoop的读取方式(一行一行读取),使用Hadoop的FileInputFormat的getSplits()
    //totalSize:统计需要读取文件的字节数总和，2.txt为7字节
    //goalSize: 7 / 你的分区数2 = 3 (byte),也就是每个分区存放3个字节
    // 7/3 =2...1，到这里还有1字节没地方放，那么根据hadoop中的切片方法，你剩余的字节(1)/每个分区的字节数(3)=33.3%>10%(1.1)那么还会生成一个新分区，所以是2+1个分区
    val rdd2 = sc.textFile("datas/source/bigtable", 2)
    rdd2.saveAsTextFile("output/output1")

    /*
    * 数据           偏移量
    * 1234567\n     12345678
    * 89\n          91011
    * 0             12
    * 一共12个字节，指定最小分区数是2，每个分区存的数据应该为12/2=6
    * 又12/6=2正好2个分区够用，那么分区0读[0,6]，分区1读[6,12],
    * 那么第一行从偏移量0开始读到偏移量为6,但又不能只读一行的一部分，所以要读完一行，那就是1234567\n
    * 第二行从本来要从偏移量6开始读的，但是已经读完第一行了就只能从第二行开始读直到读到偏移量为12，
    * 那就是89\n0读完。
    * 至此分区0就是：1234567\n
    *    分区1就是：89\n0
    * */
    sc.stop()
  }
}
