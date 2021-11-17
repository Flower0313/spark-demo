package com.atguigu.tansfrom.k_v

import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_partitionBy_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日0:05 - 周日
 * @Describe
 */
object rdd_partitionBy_1 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //将数据从单值变成元组类型才能用partitionBy，也就是只能是k-v类型
    val mapRdd = rdd.map((_, 1))

    //RDD=>PairRDDFunciton,靠的是隐式转换(二次编译),这里可以取到RDD中的value值
    /*
    * 在RDD中有一个方法是rddToPairRDDFunctions隐式转换
    * 转换逻辑，现在RDD要调用一个不属于自己类的partitionBy()方法，程序首先会知道partitionBy()方法属于
    * PairRDDFunctions类，所以在RDD中有没有一个隐式函数能将RDD转换为PairRDDFunctions类呢？并且这个隐式
    * 函数的输入参数必须是RDD类(因为隐式函数是根据参数类型匹配的),于是在RDD中找到了rddToPairRDDFunctions这个
    * 隐式函数，这个隐式函数就是将RDD类(输入类型)转换为PairRDDFunctions类(输出类型),所以此时能调用partitionBy()了。
    *
    * 总结：rddToPairRDDFunctions()就是属于RDD类型的隐式转换,有且仅能转换RDD类型
    * 1）如果经过partitionBy()的分区结果RDD按同样的规则再调用一次partitionBy()方法，那么不会产生新分区，直接将当前的分区返回。
    * 2）默认分区器有HashPartitioner、RangePartitioner、PythonPartitioner
    * 3）可以自定义分区器
    * */

    //进行奇偶分区，与coalesce区分(分区数量的改变),partitionBy是数据所在分区的位置
    mapRdd.partitionBy(new HashPartitioner(2))
      .saveAsTextFile("output/output1")


    sc.stop()
  }
}
object rdd_partitionBy_2{
  def main(args: Array[String]): Unit = {

    val conf:SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc:SparkContext = new SparkContext(conf)

    val rdd: RDD[(Int, String)] = sc.makeRDD(Array((1, "aaa"), (2, "bbb"), (3, "ccc")), 3)

    rdd.partitionBy(new MyPartition(2))

    val indexRdd = rdd.mapPartitionsWithIndex(
      (index, datas) => datas.map((index,_))
    )


    sc.stop()
  }
}

class MyPartition(num:Int) extends Partitioner{
  //设置分区数
  override def numPartitions: Int = num

  override def getPartition(key: Any): Int = {
    key match {
      case keyInt: Int =>
        if (keyInt % 2 == 0)
          0
        else
          1
      case _ =>
        0
    }

  }
}
