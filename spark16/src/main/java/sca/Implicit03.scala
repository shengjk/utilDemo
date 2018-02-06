package sca

/**
  * Created by yasaka on 2016/6/1.
  */

// 隐式参数

class SignPen{
  def write(content:String) = println(content)
}

object Util{
  implicit val signPen = new SignPen
}

object Implicit03 {

  def signForExam(name:String)(implicit signPen:SignPen): Unit ={
    signPen.write(name + " arrive for exam!")
  }

  def main(args: Array[String]) {
     import Util._
     signForExam("yasaka")
     signForExam("caiguinan")
  }
}
