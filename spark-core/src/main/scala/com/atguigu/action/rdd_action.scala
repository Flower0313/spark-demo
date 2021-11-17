package com.atguigu.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_reduce_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日0:25 - 周一
 * @Describe
 */
//reduce
object rdd_action_reduce {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4),3)
    //但是不能设置初始值，在分区间计算的时候，由于分区数据乱序，结果不固定
    val i: Int = rdd.reduce((x, y) => x + y)
    println(i)

    sc.stop()
  }
}

//collect
object rdd_action_collect {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 1, 2))

    //采集不同分区的数据按照分区顺序进行采集到Driver端内存中进行处理变成Array,是按分区顺序来执行的。
    val ints: Array[Int] = rdd.collect()
    rdd.takeOrdered(3)//排序后再拿数据

    sc.stop()
  }
}

//count
object rdd_action_count {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))

    //数据源中的数据的个数
    val l: Long = rdd.count()

    sc.stop()
  }
}

//first
object rdd_action_first {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    val i: Int = rdd.first()
    val ints: Array[Int] = rdd.take(2)
    //take拿元素可以跨分区
    sc.stop()
  }
}

//aggregate
object rdd_action_aggregate {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    /*
    * aggregate：可以接受V，返回的是聚合结果,初始值不仅参与分区内计算，还会参与分区间
    *            的计算。
    *
    * aggregateByKey:必须是(K,V),返回的是RDD,初始值只会参与分区内计算
    * */
    val res: Int = rdd.aggregate(10)(_ + _, _ + _)

    //(10+1+2)= 13分区内
    // (10+3+4)= 17分区内
    //13+17+10 = 40分区间

    sc.stop()
  }
}

//fold
object rdd_action_fold {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //当分区内和分区间的计算规则相同时用fold
    val res: Int = rdd.fold(0)(_ + _)
    sc.stop()
  }
}

//countByValue,适用与value类型
object rdd_action_countByValue {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 2, 3), 2)

    //Map(4 -> 1, 2 -> 2, 1 -> 1, 3 -> 2)
    val map: collection.Map[Int, Long] = rdd.countByValue()

    println(map)

    sc.stop()
  }
}

//countByKey,只适用于(K,V)类型
object rdd_action_countByKey {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3), ("b", 4)
    ))

    //Map(a -> 3, b -> 1),a出现了3次，b出现了1次
    val map: collection.Map[String, Long] = rdd.countByKey()

    println(map)


    sc.stop()
  }
}



































