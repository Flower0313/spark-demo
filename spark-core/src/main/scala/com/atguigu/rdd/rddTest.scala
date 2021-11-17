package com.atguigu.rdd

/**
 * @ClassName ScalaDemo-rddTest 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月22日15:00 - 周五
 * @Describe
 */
object rddTest {
  def main(args: Array[String]): Unit = {
    val array = Array(1, 2, 3, 4)

    proPositions(array.length, 3).map {
      case (start, end) => array.slice(start, end).toList
    }.foreach(println)

    //proPositions(arr.length,3).foreach(println)

    //传入的并不是数据本身，而是数据组的长度，但是这些长度能标记一个数据范围，比如数据长度=全部数据，数据长度/2=前一半数据
    def proPositions(length: Long, numSlices: Int): Iterator[(Int, Int)] = {
      //(0 until 3)循环0,1,2
      //逻辑都写在{ }代码块中，将单个元素变成(start,end)元组
      (0 until numSlices).iterator.map { i => //取出来的i是0,1,2...numSlices-1
        val start = ((i * length) / numSlices).toInt
        val end = (((i + 1) * length) / numSlices).toInt
        (start, end) //返回的是一个元组
        /*
        * 经过分析：返回的元组(start,end)
        * 其中start是一个分区号，代表属于哪个分区
        * end-start就是这个分区的数据长度(数据个数)
        * */
      }
    }


  }
}
