package sca

/**
  * Created by shengjk1 on 2016/9/22.
  */
class  Person(var age:Int=12,var name:String=""){
  private var add=""

  println("主构造器")
  def this(age:Int,name:String,add:String){
    this(age,name)
    this.add=add
    println("从构造器")
  }

}


object Person {

  def main(args: Array[String]): Unit = {
    val person =new Person(12,"a")
    val person1 =new Person()


  }

}
