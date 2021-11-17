package com.atguigu.spark.test

/**
 * @ClassName ScalaDemo-Test_1 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月01日20:24 - 周一
 * @Describe
 */
object Test_1 {
  def main(args: Array[String]): Unit = {
    val p = new Person("xiaohua")

  }
}


class Person(name: String, flag: Int) {

  def this(name: String) {
    this(name, 313)
  }

  println(this.name + ":" + this.flag)
}
