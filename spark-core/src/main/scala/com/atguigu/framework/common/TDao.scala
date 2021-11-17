package com.atguigu.framework.common

import com.atguigu.framework.util.EnvUtil
import org.apache.spark.rdd.RDD

/**
 * @ClassName ScalaDemo-TDao 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日18:11 - 周三
 * @Describe
 */
trait TDao {
  def readFile(path: String): RDD[String] = {
    EnvUtil.take().textFile(path)
  }
}
