package com.atguigu.framework.service

import com.atguigu.framework.common.TService
import com.atguigu.framework.dao.WordCountDao
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-WordCountService 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日17:33 - 周三
 * @Describe 服务层
 *           逻辑有问题看service
 */
class WordCountService extends TService {
  private val wordCountDao = new WordCountDao()

  //数据分析
  override def dataAnalysis(): RDD[(String, Int)] = {
    //todo 执行业务操作

    val lines: RDD[String] = wordCountDao.readFile("datas/source/1.txt")


    val value: RDD[(String, Int)] = lines
      //相当于先将集合(hello,hello,world)再((hello,1),(hello,1),(world,1))
      .flatMap(x => x.split(" ").map(y => (y, 1)))
      .groupBy(x => x._1)
      .map(x => (x._1, x._2.size))

    value
  }
}
