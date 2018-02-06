package sca

/**
  * Created by jk on 2016/7/18.
  */

/*
隐式参数
*/
class SignPen{
  def write(context:String)=println(context)
}
class SignPen1{
  def write(context:String)=println("aaaaaaaaaaaaaaa")
}

object Util{
  implicit val signPen=new SignPen
  implicit val signPen1=new SignPen1
}

object Implicit1 {
  def signForExam(name:String)(implicit signPen1:SignPen): Unit ={
    signPen1.write(name + " arrive for exam!")
  }

  def main(args: Array[String]) {
    import Util._
    signForExam("yasaka")
  }
}
