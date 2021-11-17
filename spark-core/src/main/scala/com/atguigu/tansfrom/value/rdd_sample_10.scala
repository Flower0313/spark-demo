package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_trans_10 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日11:11 - 周六
 * @Describe sample
 */
object rdd_sample_10 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))


    /**
     * @param1 抽取数据后是否将数据放回,true(放回)|false(丢弃)，比如第一次抽到了2，若为false，那么下次就抽不到2了,所以数据不会重复
     * @param2 每条数据可能被抽取的概率(0.0-1.0),配合seed一起使用
     *         1_如果参数1为false:当你确定seed时随机算法就已经确定，每条数据的概率也确定，只有>0.4的数据能出来
     *         2_如果参数1为true:表示数据源中每条数据被抽取的可能次数
     * @param3 抽取数据时随机种子,如果不传递则使用系统时间，传了就固定了
     */
    //伯努利
    println(rdd.sample(withReplacement = false,
      0.4,
      1)
      .collect().mkString(","))

    /*
    * 泊松
    * 第二个参数时表示每个元素取次数的期望值
    *
    * */
    println(rdd.sample(withReplacement = true,
      2)
      .collect().mkString(","))

    sc.stop()
  }
}


object rdd_sample_11{
  def main(args: Array[String]): Unit = {

  }
}
