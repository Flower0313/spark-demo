package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_sortBy_14 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日21:56 - 周六
 * @Describe sortBy
 */
class rdd_sortBy_14 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(3, 1, 4, 1, 5, 6, 9), 2)

    /*
    * 会shuffle，也就是会打乱分区的数据进行排序，再按序号进行填充
    * 比如【6,3,5,1,2,4】分成两个区是【6,3,5】【1,2,4】，
    * 然后用sortBy后【1,2,3】和【4,5,6】
    *
    * 它会另起一个job来记录顺序
    * */
    val value: RDD[Int] = rdd.sortBy(x => x)
    sc.stop()
  }
}

class rdd_sortBy_14_2 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.makeRDD(List(("1", 1), ("11", 2), ("2", 3)), 2)

    //按照元组的key来排(字典序)，所以和hbase中一样,"1"<"11"<"2"
    rdd.sortBy(x => x._1,false)//参数二表示降序


    sc.stop()
  }
}
