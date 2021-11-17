package com.atguigu.req

/**
 * @ClassName ScalaDemo-test 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日11:47 - 周二
 * @Describe
 */
object test {
  def main(args: Array[String]): Unit = {
    val list = List("1_2_ab,flower", "313_22_holden,xiao")

    val rddm: Seq[(String, Int)] = list.flatMap {
      x => {
        val datas: Array[String] = x.split("_")
        val strings: Array[String] = datas(2).split(",")
        //strings //是集合类型可以返回,返回后最后还是会被拆开
        strings.map(x => (x, 1)) //将(ab,flower)拆成(ab,1)和(flower,1)返回
      }
    }
    println(rddm)
    /*
    * 与map的对比，map进去2元素，输出两个元素，flatMap进去2个元素输出4个元素，因为后者会拆分集合，使之扁平化
    * */
    val list1: Seq[Array[(String, Int)]] = list.map {
      x => {
        val datas: Array[String] = x.split("_")
        val chips: Array[String] = datas(2).split(",")
        chips.map(x => (x, 1))
      }
    }
    println(list1)

  }
}

//迭代器
object test_2 {
  def main(args: Array[String]): Unit = {
    val ints: Iterator[Int] = Iterator(1, 2, 3, 4)
    val it = Iterator("Baidu", "Google", "Runoob", "Taobao")

    var sum: Int = 0
    while (ints.hasNext) {
      sum += ints.next()
    }
    println(sum)
  }
}

//sortBy
object test_3 {
  /*
  *
  * 在Ordering.scala中的def Tuple3中进行比较.
  * 比较方式：先取两个元组，逐个对其中的元素进行比较，比如
  * 元组A(10,9,10)和B(10,10,9),三元组调用Tuple3()，然后有3个compare
  * 首先通过compara1比较A._1和B._1，若compara1=0，则继续走
  * compare2,也就是比较A._2和B._2，若compare2!=0则比出了结果.然后小的数和下一个去跑。
  *
  * 比如A(10,10,9)和B(10,9,10)和C(10,10,10)
  * 1.A先和B比较，B小
  * 2.B和C比较，B小
  * 3.C和A比较
  * */
  def main(args: Array[String]): Unit = {
    val tuples: List[(String, (Int, Int, Int))] = List(("a", (10, 10, 9)), ("b", (10, 9, 10)), ("c", (10, 10, 10)))
    val res: List[(String, (Int, Int, Int))] =
      tuples.sortBy(x => x._2) //这里x是(String,(Int,Int,Int)),底层会调用sorted,两个两个比较

    println(res)
  }
}

//union
object test_4 {
  def main(args: Array[String]): Unit = {
    val list1: List[(String, Int)] = List(("a", 1), ("b", 31), ("c", 11), ("d", 20))

    val list2: List[(String, Int)] = List(("a", 2), ("b", 69), ("e", 90))

    val tuples: List[(String, Int)] = list1.union(list2)

    /*
    * List((a,1), (b,31), (c,11), (d,20), (a,2), (b,69), (e,90))
    * */
    println(tuples)
  }
}

object test_5 {
  def main(args: Array[String]): Unit = {
    val map: Map[String, List[Int]] = Map(("a", List(1, 2)), ("b", List(3, 4)))

    val values: Iterable[List[Int]] = map.values

    println(values)
  }
}

object test_6 {
  def main(args: Array[String]): Unit = {
    val ints: List[Int] = List(1, 2, 3)

    val map: Map[String, Int] = Map(("a", 1), ("b", 2))

    ints.foreach(x => println(x))
    map.foreach(x => println(x))
  }
}

object test_7 {
  def main(args: Array[String]): Unit = {
    val tuples: Iterable[(String, Int)] = Iterable(("a", 1), ("b", 3), ("c", 2))
    val list: List[(String, Int)] = tuples.toList

    println(list.sortBy(x => x._2)(Ordering.Int.reverse))
  }
}

object test_8 {
  def main(args: Array[String]): Unit = {
    val list: List[(String, Int)] = List(("23", 1), ("10", 2), ("1", 3), ("90", 4))

    val tuples: List[(String, Int)] = list.sortBy(x => x._1.toInt)

    println(tuples)
  }
}

//滑窗、zip
object test_9 {
  def main(args: Array[String]): Unit = {
    //将【1,2,3,4,5】 => 【(1,2),(2,3)...(4,5)】的形式
    val list1: List[Int] = List(1, 2, 3, 4, 5, 6, 7)
    list1.sliding(2, 1).foreach(println)

    //1,2,3,4,5
    //2,3,4,5
    list1.zip(list1.tail).foreach(println)

  }
}

object test_10 {
  def main(args: Array[String]): Unit = {
    val list: List[((String, String), Int)] = List((("a", "b"), 1), (("b", "c"), 1))

    val list1: List[((String, String), Int)] = list.map(x => x)
    println(list1)

    val value: Seq[((String, String), Int)] = list.flatMap(x => List(x))
    println(value)

  }
}

object test_11 {
  def main(args: Array[String]): Unit = {
    val list: List[(Int, Int)] = List((1, 2), (2, 3), (4, 5))
    val list1: List[(Int, Int)] = List((3, 3), (4, 4), (4, 5))

    list.foreach(
      t => {
        println(list1.contains(t))
      }
    )
  }
}

//reduce
object test_12 {
  def main(args: Array[String]): Unit = {
    val list: List[(Int, Int)] = List((1, 2), (2, 3), (4, 5),(2,2))

    //(A1,A2) => A1,将相邻的两个元组相加再返回一个处理完的元组
    println("reduce:"+list.reduce((x, y) => {
      (x._1 + y._1, x._2 + y._2)
    }))
  }
}








