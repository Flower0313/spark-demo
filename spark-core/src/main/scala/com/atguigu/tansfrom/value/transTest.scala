package com.atguigu.tansfrom.value

/**
 * @ClassName ScalaDemo-transTest 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日19:09 - 周五
 * @Describe
 */
object transTest {
  def main(args: Array[String]): Unit = {
    val list = List("a", "b", List("c", List("def")))
    println("flatMap:" + list.flatMap(x => x :: List("*")))
    println("map:" + list.map(x => x :: List("*")))
  }
}

object transTest_2 {
  def main(args: Array[String]): Unit = {
    val x: Int = 313
    println(withScope1(add(3)))
    println(withScope1(add(x)))


    println(withScope2(add))
    println(withScope2(x => x + 313))

  }

  def add(x: Int): Int = {
    x + 313
  }

  //接收一个调用的函数
  def withScope1(body: => Int): Int = {
    body
  }

  //接收一个定义的函数
  def withScope2(head: (Int => Int)): Int = {
    head(313)
  }
}











