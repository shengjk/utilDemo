package sca

/**
  * Created by shengjk1 on 2016/9/27.
  */
object function1 {
  def main(args: Array[String]): Unit = {
    import scala.math._
    val num=3.14
    val fun=ceil _
    println(fun(num))
  }

}
