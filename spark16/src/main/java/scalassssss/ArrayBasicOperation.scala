package scalassssss

/**
  * Created by 小省 on 2016/7/4.
  */
object ArrayBasicOperation {
  def main(args: Array[String]) {
    //array数组操作  默认为定长数组   java中数组也是定长的额
    //访问() java[]
    /*
    在JVM中，Scala的Array以Java数组方式实现。
    示例中的数组在JVM中的类型为java.lang.String[]。
    Int、Double或其他与Java中基本类型对应的数组都是基本类型数组。
    举例来说，Array(2,3,5,7,11)在JVM中就是一个int[]。
     */

    //定义一个定长数组  10个null
    val nums = new Array[Int](10)

    //数组的apply方法
    val s = Array("Heello", "hi")
    //改变数组元素的值
    s(0) = "hhhhh"

    //数组遍历
    for (x <- 0 until s.length) {
      println(s(x))

    }



    //变长数组 ArrayBuffer 相当于java中的arrayList 不会去重
    import scala.collection.mutable.ArrayBuffer
    val b = ArrayBuffer[Int]()
    // +=在尾端添加元素
    b += 1
    // 在尾端添加多个元素，以括号包起来
    b +=(1, 2, 3, 4, 5)
    //在末尾添加数组
    b ++= Array(5, 6, 7, 8)
    //移除后5个元素   尾端添加或移除元素是一个高效
    b.trimEnd(5)
    //任意位置添加或移除 不高效   在第2个位置添加9，8,7
    b.insert(2, 9, 8, 7)
    //
    b.remove(2, 3)

    for (x <- 0 until b.length) {
      println(b(x))
    }

    for (elem <- b) {
      println(elem)
    }

    /*
    如果要从数组的尾端开始，遍历的写法为：
  (0 until a.length).reverse
    // Range(..., 2, 1, 0)

     */

    //array和ArrayBuffer互转
    //反过来，调用a.toBuffer可以将一个数组a转换成一个数组缓冲。
    b.toArray
    println(b)
    for (elem <- b) {
      println(elem)

    }



    //数组转huan

    val a = Array(3, 4, 5, 6, 7, 8)
    /*
    for(...) yield循环创建了一个类型与原始集合相同的新集合。
    如果你从数组出发，那么你得到的是另一个数组。
    如果你从数组缓冲出发，那么你在for(...) yield之后得到的也是一个数组缓冲。
     */
    //    val resu=for (elem <-a  if elem%2==0) yield 2*elem
    //用函数达到同样的效果
    val resu = a.filter(_ % 2 == 0).map(2 * _)
    for (resu <- resu) {
      print(resu + " ")
    }

    /*array arraybuffer同样适用
    要使用s u m 方法，元素类型必须是数值类型：
    要么是整型，要么是浮点数或者BigInteger/BigDecimal。
     */
    println("max " + resu.max)
    println("sum " + resu.sum)

    //sorted 方法将数组或数组缓冲排序并返回经过排序的数组或数组缓冲，
    // 这个过程并不会修改原始版本：
    for (elem <- resu) {
      println(elem)
    }
        val c=resu.sorted
    println(c.mkString("<"))
    println(c.mkString("<","|",">"))

    /*
    你可以直接对一个数组排序，但不能对数组缓冲排序：

  val a = Array(1, 7, 2, 9)

  scala.util.Sorting.quickSort(a)




  对于min、max和quickSort方法，元素类型必须支持比较操作，这包括了数字、字符串以及其他带有Ordered特质的类型




  最后，如果你想要显示数组或数组缓冲的内容，可以用mkString方法，它允许你指定元素之间的分隔符。该方法的另一个重载版本可以让你指定前缀和后缀。例如：

  a.mkString(" and ")

    // "1 and 2 and 7 and 9"

  a.mkString("<", ",", ">")

    // "<1,2,7,9>"

和toString相比：

  a.toString

    // "[I@b73e5 "

    // 这里被调用的是来自Java的毫无意义的toString方法

  b.toString

    // "ArrayBuffer(1, 7, 2, 9)"

    // toString方法报告了类型，便于调试
     */
  }

}
