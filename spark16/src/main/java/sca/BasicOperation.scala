package sca

object BasicOperation {
  def main(args: Array[String]): Unit = {
    
    // 基础语法
//    val age = 22 //常量 immutable class final
//    var age = 22 //变量
//    age = 23
//    println(age)
    
//    val name : String = "yasaka"
//    println(name)
//    
//    val n : Any = "iloveuguys"
//    
//    val name1, name2, name3 :String = null
//    val a,b,c = 1
//    println(b)
    
//    println( 1 to 10 )
//    println("hello" intersect "world") // 隐式转换 StringOps
//    
//    var count = 0
//    count += 1
//    
//    import scala.math._
//    println(sqrt(2))
//    println(pow(2,4))
//    println(min(4, Pi))
//    
//    val list = List(1,2,3,4,5,5)
//    println(list.distinct)
    
    // apply 函数！！！
//    println("yasaka love u guys"(5))
//    println("yasaka love u guys".apply(5))
    
    // 条件控制 if else
//    val age = 30
//    println(if (age >18 ) 1 else 0 )
//    val isAdu = if (age >18 ) "adult" else 0 
//    println(isAdu)
//    
//    val isChild = if(age < 18) "children" // else不写返回类型是Unit
//    
//    var a , b ,c = 1
//    val f = if(1 < 2) {b = b + 1 ; c = c + 2 ; c } 
//    println(f)
    
    // 输入与输出
//    println("hello")
//    print("hello")
//    val name = readLine("Welcome! ")
//    println(name)
      
    // 循环 for
//    var n = 10
//    while(n > 0){
//      println(n)
//      n -= 1
//    }
    
//    for(i <- "i love u guys"){
//      print(i)
//    }
    
//    import scala.util.control.Breaks._
//    breakable{
//      var n = 10
//      for(i <- "i love u guys"){
//        if(n == 3) break;
//        print(i)
//        n -= 1
//      }
//    }
    
    // 请同学们把九九乘法表用上面学的知识给打印出来！
    
//    for(i <- 1 to 20 if i % 2 == 0 ; if i % 5 > 3) {println(i)}
//    
//    val temp = for(i <- 1 to 10) yield i 
//    println(temp)
    
    // 这个和python不一样，python是弱类型，scala不一样
//    var age:Any = 20
//    age = "yasaka"
    
    // 数组操作
    // 数据 放在 数据结构 再来写算法
    // Array ArrayList  <==> Array ArrayBuffer
//    val a = new Array[Int](10)
//    println(a(1))
//    val b = new Array[String](10)
//    b(1) = "yasaka"
//    println(b(1))
//    
//    val c = Array("i","love","xuruyun")
//    c(2) = "liangyongqi"
//    println(c(2))
//    
//    val d = Array("xuruyun", 30)
//    println(d.length)
//    d(1)
////    d(2) = "yasaka"
    
    import scala.collection.mutable.ArrayBuffer
//    val a = ArrayBuffer[Int]()
//    a += 1
//    a += (2,3,4,5)
//    a ++= Array(6,7,8)
//    a.trimEnd(2)
//    
//    a.insert(5,6)
//    a.insert(5,6,7,8,9,10)
//    a.remove(1,3)
//    println(a)
    
//    val a = Array("java is only oo","python is oo and fp","scala is oo and fp too!")
//    val b = ArrayBuffer("java","python","scala")
//    val aArrayBuffer = a.toBuffer
//    println(aArrayBuffer)
//    val arr = b.toArray
//    println(arr)
//    
//    for(i <- 0 until b.length){
//      println(b(i))
//    }
    
//    val a = Array(4,3,2,5)
//    val sum = a.sum
//    println(sum)
//    val max = a.max
//    println(max)
//    
//    scala.util.Sorting.quickSort(a)
//    for(i <- 0 until a.length){
//      println(a(i))
//    }
//    
//    // mkString 一般用于array
//    println(a.mkString)
//    println(a.mkString("<",",",">"))
//    // toString 一般咱们用于ArrayBuffer
//    val buffer = ArrayBuffer[Int]()
//    buffer ++= Array(1,2,3)
//    println(buffer.toString()) //ArrayBuffer(1, 2, 3)
//    println(buffer.mkString) //123
  }
}