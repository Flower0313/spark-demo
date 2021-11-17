package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_trans_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日19:17 - 周五
 * @Describe 算子的分区情况
 */
object rdd_trans_3 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    /*
    * 情况一：numSlices=1,这个分区中就是【1,2,3,4】
    * 此时只有一个分区，计算过程是线程1---1执行完后执行线程2---1，当元素1执行完后开始执行元素2，于是是
    * 线程1---2执行然后再线程2---2执行...依次执行到元素4，且数据的执行是有序的。
    *
    * 情况二：numSlices=2，分区0中是【1,2】 ， 分区1中是【3,4】
    *
    * 这种情况分区0中元素1肯定在元素2之前执行，分区1中元素3肯定在元素4之前执行，由于是并行计算,
    * 元素1和元素3的执行顺序就说不好了,元素2和元素4谁先开始也不好说。
    *
    * 总结：不同分区之间无序，相同分区内有序
    *
    * */
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    val mapRdd1 = rdd.map(x => {
      println("线程1-------" + x)
      x
    })

    //RDD转换，类似装饰者，将mapRdd1会和mapRdd2连接起来
    //执行的时候按分区执行，先走mapRdd1中的分区0再走mapRdd2的分区0
    val mapRdd2 = mapRdd1.map(x => {
      println("线程2-------" + x)
      x
    })

    mapRdd2.collect()

    sc.stop()
  }

}
