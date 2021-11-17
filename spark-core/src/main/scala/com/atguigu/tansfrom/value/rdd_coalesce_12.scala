package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_coalesce_12 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日14:35 - 周六
 * @Describe coalesce
 */
object rdd_coalesce_12 { //RDD分区>划分的分区(缩减分区)
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    /*
    * 情况1：List(1,2,3,4)分4个分区【1】【2】【3】【4】，coalesce(2)改成2个分区,于是【1,2】、【3,4】
    *
    * 情况2：List(1,2,3,4,5,6)分3个分区,coalesce(2)改成2个分区，则是【1,2】、【3,4,5,6】
    * 为什么会这样放呢？
    * 因为coalesce不会将数据打乱重新组合， 它只是改成2个区，因为本来是【1,2】【3,4】【5,6】
    * 后面改成了2个区，所以第一个区还是【1,2】但是走到第二个区时应该要结束了，但发现后面还有一个区，这时
    * 就把分区2合并到了分区1上就变成了【3,4,5,6】，然后就改成了2个分区，因为分区的数据不能拆分，所以
    * 不能单拆一个5或单拆一个6出去，他们两个必须在一起走；但这样会导致数据倾斜。其实分区逻辑就是整数除的那个算法。
    *
    * 情况3：如果你不满足情况2的数据倾斜，那就在coalesce的参数二添加true，打开shuffle，这样每个分区的数据就
    * 能拆分了打乱了均衡放在2个分区中，分区中数据个数均衡，但数据不一定有序【1,4,5】、【2,3,6】
    *
    * */

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6), 3)
    val value: RDD[Int] = rdd.coalesce(2, true)
    value.saveAsTextFile("output/output1")
    sc.stop()
  }

}

object rdd_coalesce_12_2 { //RDD分区<划分分区(增加分区)
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6), 2)

    /*
    * 情况1：【1,2,3】和【4,5,6】分成3个分区且不shuffle，可结果是还是没变，因为不shuffle的话是没有任何意义的
    * ，划分多的分区永远是空的。
    *
    * 情况2：在1的条件下，进行shuffle，就能将分区的数据打乱来重新分区，所以这就会变成3个分区，每个分区里面有2个数据，
    * 具体数据顺序不确定
    *
    * */
    val value: RDD[Int] = rdd.coalesce(3, true)
    value.saveAsTextFile("output/output1")
    sc.stop()
  }
}

object rdd_coalesce_12_3 { //RDD分区<划分分区(增加分区)
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6), 2)

    //底层就是coalesce，只不过shuffle默认设置为true了
    val value: RDD[Int] = rdd.repartition(3)
    value.saveAsTextFile("output/output1")
    sc.stop()
  }
}
/*
* 总结：
* 1)缩减分区:coalesce，如果想要数据均衡就设置shuffle为true
* 2)扩大分区:repartition,它默认走shuffle
*
*
* */

















