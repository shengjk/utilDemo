package sca

/**
  * Created by shengjk1 on 2016/8/16.
  */
class Account private(val id:Int,val name:String){
  private  var value=0;
  println("==============")
  def increament(){print("函数")}
}

object Account {

  def main(args: Array[String]): Unit = {

  }
}
