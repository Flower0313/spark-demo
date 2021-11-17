package com.atguigu.spark.test

/**
 * @ClassName ScalaDemo-Tset_3 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年11月01日22:58 - 周一
 * @Describe
 */
object Tset_3 {
  def main(args: Array[String]): Unit = {
    val flower: EventLoop = new EventLoop("flower") {
      override def onReceive: Unit = { //重写了方法，这就是一个具体的类，而不是抽象类，它拥有start()方法,而EventLoop只是提供一个模版
        println("Event线程"+Thread.currentThread().getName)
      }
    }
    flower.start()
    println("主线程"+Thread.currentThread().getName)


    /*val loop: EventLoop = new EventLoop("") {

    }
    loop.start()*/
  }
}

abstract class EventLoop(name: String) {
  private val eventThread = new Thread() {
    override def run(): Unit = {
      onReceive()//在这个方法生效前，就已经被重写了
    }
  }

  def start() = {
    eventThread.start()
  }

  def onReceive(): Unit = println("父类")
}