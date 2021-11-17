package com.atguigu.framework.common

import com.atguigu.framework.util.EnvUtil
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-TApplication 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日18:00 - 周三
 * @Describe 抽象出公共的部分
 */
trait TApplication {
  //使用控制抽象传递需要执行的逻辑代码,柯里化写法,op传的是你的逻辑操作
  def start(master: String = "local[*]", app: String = "Application")(op: => Unit) = {
    val sparkConf = new SparkConf().setMaster(master).setAppName(app)
    val sc: SparkContext = new SparkContext(sparkConf)
    EnvUtil.put(sc)

    try {
      op
    } catch {
      case ex: Throwable => println(ex.getMessage)
    }
    sc.stop()
    EnvUtil.clear()
  }
}
