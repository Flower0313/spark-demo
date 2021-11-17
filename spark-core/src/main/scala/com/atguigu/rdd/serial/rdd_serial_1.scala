package com.atguigu.rdd.serial

import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-rdd_serial_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月25日13:58 - 周一
 * @Describe
 */
object rdd_serial_1 {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.{SparkConf, SparkContext}

    val conf: SparkConf = new SparkConf().setAppName("rdd").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark", "atguigu"))

    val search: Search = new Search("hello")

    // Driver：算子以外的代码都是在Driver端执行
    // Executor：算子里面的代码都是在Executor端执行
    search.getMatch1(rdd).collect().foreach(println)

    //闭包检查：如果匿名函数中使用到了外部自定义类中定义的变量，会进行检查

    //search.getMatche2(rdd).collect().foreach(println)


    sc.stop()
  }

}

//类的构造函数是类的属性,构造参数也需要进行闭包检测

class Search(query: String) extends Serializable {
  //就是一个被封装的fitler方法而已
  def isMatch(s: String): Boolean = {
    s.contains(this.query)//所以和类扯上关系，类需要被序列化
  }

  // this.isMatch()表示Search这个类的对象，程序在运行过程中需将Search对象序列化后传递到Executor端
  def getMatch1(rdd: RDD[String]): RDD[String] = {
    rdd.filter(this.isMatch)//filter会将rdd每个值传到isMatch方法中判断是否包含目标值,因为底层会将值作为s传入isMatch
    //不懂可以看functionT9第40行
  }

  // 属性序列化案例
  def getMatche2(rdd: RDD[String]): RDD[String] = {
    val q = this.query//将类变量赋值给局部变量也能解决序列化问题
    rdd.filter(x => x.contains(q))
  }
}

//样例类自带序列化，在网络中传递的类需要序列化
case class Student(val name:String)











































