package sca

/**
  * Created by yasaka on 2016/6/1.
  */
object FunctionProgramAdvanced {

  def main(args: Array[String]) {
    // 集合的操作 Array ArrayBuffer
    // Map Tuple
    // 1 to 10 Range
    // Iterable是所有集合trait根trait, Collection
    // Seq Set Map
    // scala.collection.immutable scala.collection.mutable

    // Seq --> Range ArrayBuffer List LinkedList trait!!
//    val list = List(1,2,3,4) // apply是不是定义在List的伴生对象里面的啊！！！
//    println(list.head )
//    println(list.tail)
//    // ::
//    println(0::list)
//    // Nil 代表长度为0的List
//    println(0::Nil)

    // 用递归函数来给List每个元素都加上前缀或后缀
//    def decorator(l:List[Int], prefix:String): Unit ={
//      if(l !=Nil){
//        println(prefix+l.head)
//        decorator(l.tail,prefix)
//      }
//    }
//    decorator(List(1,2,3,4),"+")

    // LinkedList 代表可变的列表！！
//    val list = scala.collection.mutable.LinkedList(1,2,3,4,5,6)
//    println(list.elem)
//    println(list.next)

    // 使用While循环静里面的每个元素乘以2
//    var currentlist = list
//    while (currentlist!=Nil){
//        currentlist.elem = currentlist.elem * 2
//        currentlist = currentlist.next
//    }
//    println(list)

    // 使用循环将第一个元素开始，每隔一个元素，乘以2
//    val list = scala.collection.mutable.LinkedList(1,2,3,4,5,6)
//    var currentList = list
//    var first = true
//    while (currentList!=Nil && currentList.next!=Nil){
//      if(first) {currentList.elem = currentList.elem*2; first=false}
//      currentList = currentList.next.next
//      if(currentList!=Nil) currentList.elem = currentList.elem * 2
//    }
//    println(list)

    // Set
//    val s = Set(1,3,4)
//    println(s + 2)
//    val s = new scala.collection.mutable.HashSet[Int]()
    //    s += 1
    //    s += 2
    //    s += 5
    //    println(s)

    // LinkedHashSet
//    val s = new scala.collection.mutable.LinkedHashSet[Int]()
//    s += 1
//    s += 2
//    s += 5
//    println(s)

    // SortedSet
//    val s = scala.collection.mutable.SortedSet("orange","apple","grape")
//    println(s)
//    s += "banana"
//    println(s)

    // high-order function
    // map flatMap reduce reduceLeft foreach
//    val list = List("orange","apple","grape").map("i like " + _)

//    println(list)
//    List("orange","apple","grape").map("i like " + _).foreach(println(_))

    // flatMap = map + flat
//    List("hello every one", "i love xuruyun").flatMap(_.split(" ")).foreach(println(_))

    // zip
//    val list = List("yasaka","xuruyun","mozart").zip(List(30,18,-18))
//    println(list)

    // spark编程无非就是针对RDD的编程 而RDD就是集合！只不过内部是分布式的集合！
  }
}
