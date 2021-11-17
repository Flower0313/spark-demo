package com.atguigu.tansfrom.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-rdd_distinct_11 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日13:49 - 周六
 * @Describe distinct去重
 */
object rdd_distinct_11 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4))

    //scala中的distinct底层是使用HashSet来去重
    List(1, 1, 2, 2).distinct

    //spark中distinct是map(x => (x, null)).reduceByKey((x, _) => x, numPartitions).map(_._1)
    /*
    * 步骤1：将(1,2,3,4,1,2,3,4)=>(1,null)(2,null)(3,null)(4,null)(1,null)...(4,null)
    * 步骤2：聚合方式就是(x,_)=>x，其中_是模式匹配，其意思是取相同key的来做聚合,x是相同key的value值，规则是只看第一个vlaue值第二个value值不管
    *       (1,null)(1,null) => (null,null) => null
    *       结果变成(1,null)(2,null)(3,null)(4,null)
    * 步骤3：再用map取元组的第一个元素(1)(2)(3)(4)
    * */
    val distinctRdd: RDD[Int] = rdd.distinct()

    distinctRdd.collect().foreach(println)



    sc.stop()
  }
}
