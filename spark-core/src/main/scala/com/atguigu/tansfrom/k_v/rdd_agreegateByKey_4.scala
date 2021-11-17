package com.atguigu.tansfrom.k_v

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_agreegateByKey_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月24日10:07 - 周日
 * @Describe agreegateByKey与foldByKey
 */
object rdd_agreegateByKey_4 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    /*
   * 需求:取出每个分区的最大值再相加
   * 先分两个区，每个区内先聚合:(a,【1,2】)、(a,【3,4】)取分区的最大值
   * 分区间：(a,2)(a,4) => (a,6)
   * */
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3), ("a", 4)
    ), 2)

    /*
    * 柯里化：
    * 1.第一个参数列表
    *   1>初始值，一般取0，这样第一次比较0和任何值比较都是0小，任何值大，所以能取到我们传的第一个值
    * 2.第二个参数列表
    *   1>第一个参数表示分区内计算规则
    *   2>第二个参数表示分区间计算规则
    *
    * 看方法的源码可知，最终返回值类型和初始值类型一致
    * */
    val aggRdd: RDD[(String, Int)] = rdd.aggregateByKey(0)(
      (x, y) => math.max(x, y), //分区内选最大值
      (x, y) => x + y //分区间相加
    )
    //简化写法
    /*val aggRdd2: RDD[(String, Int)] = rdd.aggregateByKey(0)(
      math.max, //分区内选最大值
      _ + _ //分区间相加
    )*/

    //当然分区内和分区间的计算规则能相同，这样就可以简化为foldBykey的写法
    val aggRdd3: RDD[(String, Int)] = rdd.aggregateByKey(0)(
      (x, y) => x + y, (x, y) => x + y
    )

    //简化成foldByKey的写法
    rdd.foldByKey(0)((x, y) => x + y)


    aggRdd.collect().foreach(println)


    sc.stop()
  }
}

object rdd_agreegateByKey_4_2 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ), 2)

    /*
    * 源码：
    * def aggregateByKey[U: ClassTag](zeroValue: U, partitioner: Partitioner)(seqOp: (U, V) => U,
      combOp: (U, U) => U)
    * 这里我们传的是(0,0),所以U是(Int,Int)类型(因为ClassTag会固定你传的类型)，seqOp的V是我们分区中的value值(这是在PairRDDFunctions[K, V](self: RDD[(K, V)]))声明的,
    * 简而言之，V就是我们分区中循环的value值
    * 所以(U,V)=>U就相当于((0,0),1) => (0+1,0+1) =(1,1)再((1,1),2) => (3,2) 这是我们自己写的逻辑
    * (U,U)=>U就是每个分区之间的计算逻辑，这里每个U都是元组的形式比如key为a的计算方式为((3,2),(6,1)) => (9,3) 其中的运算逻辑也是我们写
    * */
    //需求，计算每组key的平均值,初始值(0,0)第一个0是数值和，第二个0是个数和
    val value: RDD[(String, (Int, Int))] = rdd.aggregateByKey((0, 0))(
      (t, v) => (t._1 + v, t._2 + 1), //((0,0),1) => (0+1,0+1) =(1,1)再((1,1),2) => (3,2) => 【a,(3,2)】
      (t1, t2) => (t1._1 + t2._1, t1._2 + t2._2) //(3,2)+(6,1) = (9,3) =>【a,(9,3)】
    )

    //方法一：计算统计后的平均值
    val resRdd: RDD[(String, Int)] = value.map(x => (x._1, x._2._1 / x._2._2))

    //方法二：
    value.mapValues{
          case (sum, count) => sum / count
      }


    resRdd.collect().foreach(println)

    sc.stop()
  }
}






































