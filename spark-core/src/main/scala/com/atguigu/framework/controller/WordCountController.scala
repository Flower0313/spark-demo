package com.atguigu.framework.controller

import com.atguigu.framework.common.TController
import com.atguigu.framework.service.WordCountService

/**
 * @ClassName ScalaDemo-WordCountController 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日17:33 - 周三
 * @Describe 控制层
 * 输出有问题看controller
 */
class WordCountController extends TController{
  private val wordCountService = new WordCountService();

  //调度
  override def execute(): Unit = {
    val value = wordCountService.dataAnalysis()
    value.collect().foreach(println)
  }
}
