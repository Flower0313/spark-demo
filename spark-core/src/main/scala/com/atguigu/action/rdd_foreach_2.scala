package com.atguigu.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_foreach_2 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日11:06 - 周一
 * @Describe foreach
 */
object rdd_foreach_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    //collect会将RDD方法转换为Array以分区形式进行采集再循环，所以是有序的,在Driver端内存集合循环遍历
    rdd.collect().foreach(x => println(x)) // 1,2,3,4 ,将collect()会按分区号将数据拉取回来再按分区顺序打印

    println("-------------")

    //在Executor端分布式打印,所以打印顺序不一致
    rdd.foreach(println) //3,1,4,2 分区间打印顺序随机乱序，不按分区号排序,但分区内打印顺序一致，

    /*
    * 算子：
    *   RDD方法和Scala集合方法不一样
    *   集合对象的方法都是在同一个节点的内存中完成的；
    *   RDD方法是将计算逻辑发送到Executor执行的，分布式执行；
    *   RDD的方法外部的操作都是在Driver端执行的，方法内部逻辑代码是在Executor端执行
    *   比如:上述的collect()将RDD集合转换成了Array集合(foreach变成了Array的调用方法)，所以在Driver端执行,而直接调foreach是RDD的方法
    *   所以在Executor执行。
    * */
    sc.stop()
  }
}

object rdd_foreach_2_3 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    val user = new User()
    //RDD算子中是包含闭包操作的,所以有闭包检测,就算你不调用也会检查出来你的类序列化了吗
    rdd.foreach(
      //直接在Executor端调用user对象的属性会报错：对象没有被序列化
      //因为User是在Driver端，而你使用user的属性在Executor端,所以要经过网络传递则需要被序列化
      t => println("age=" + (user.age + t))
    )
    sc.stop()
  }
}

//需要被序列化
class User extends Serializable {
  var age: Int = 30
}

//或者直接使用样例类传输,因为会自动混入序列化特质
case class User2(){
  val age :Int = 30
}






















