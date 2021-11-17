package com.atguigu.framework.dao

import com.atguigu.framework.common.TDao
import org.apache.spark.rdd.RDD


/**
 * @ClassName ScalaDemo-WordCountDao 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日17:34 - 周三
 * @Describe 持久层
 * 数据有问题就直接找Dao
 */
class WordCountDao extends TDao{
  //调用父类的readFile方法
  override def readFile(path: String): RDD[String] = super.readFile(path)
}
