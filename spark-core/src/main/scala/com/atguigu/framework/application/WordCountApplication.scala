package com.atguigu.framework.application

import com.atguigu.framework.common.{TApplication, TController}
import com.atguigu.framework.controller.WordCountController
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName ScalaDemo-WordCountApplication 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日16:59 - 周三
 * @Describe 应用层
 */
//继承App之后就不用写main了
object WordCountApplication extends App with TApplication {
  //启动应用程序,因为只有一个参数，小括号省略
  start() {
    //变化的逻辑代码
    val controller: TController = new WordCountController()
    controller.execute()
  }
}
