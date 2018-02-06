package scalassssss

import scala.collection.mutable.ArrayBuffer

/**
  * Created by jk on 2016/7/11.
  */
/*
集合  所有不可变集合的改变都会新产生一个集合
     序列seq 先后次序 包括数组和列表
    集set 没有先后顺后
    映射 map
所有集合都扩展自iterable特质
 */
object Collectionssssss {
  def main(args: Array[String]) {
    //列表 要么为Nil 要么一个head元素加上一个tail列表
    val digits = List(4, 2)
    println(digits)
    println(digits.head)
    println(digits.tail)

    //在头部添加
    println(9 :: digits)
    println(digits)
    for (a <- digits) {
      println(a)
    }

    //可变列表  数据类型可以不一样
    val lst = scala.collection.mutable.LinkedList(1, 3, 4, 5)
//    while (lst != Nil) {
//      lst = lst.next
//      println(lst)
//    }

    //集
    println(Set(2.0, 1))
    val a = Set(1, 2, 3, 4, 5, 6)
    for (aa <- a) {
      println(aa)
    }
    //链式哈希集 按插入顺序排序
    val week = scala.collection.mutable.LinkedHashSet(1, 2, 3, 4, 5, "a")
    for (a1 <- week) {
      println(a1)
    }
    //排序  不支持混合
    val week1 = scala.collection.mutable.SortedSet(1, 2, 3, 4)
    for (a2 <- week1) {
      println(a2)
    }


    println(0+:Vector(1,2,3):+5)
  }

}
