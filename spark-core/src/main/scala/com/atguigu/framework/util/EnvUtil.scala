package com.atguigu.framework.util

import org.apache.spark.SparkContext

/**
 * @ClassName ScalaDemo-EnvUtil 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月27日18:17 - 周三
 * @Describe 环境工具类,可以放上下文对象在线程池中
 */
object EnvUtil {
  private val scLocal = new ThreadLocal[SparkContext]()

  def put(sc:SparkContext)={
    //以当前线程为key,sc为value放入
    scLocal.set(sc)
  }

  def take()={
    //会自动搜寻以当前线程为key的value值,也就是sc值
    scLocal.get()
  }

  def clear()={
    //删除以当前线程为key的值
    scLocal.remove()
  }
}
