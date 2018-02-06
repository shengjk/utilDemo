package sca

/**
  * Created by yasaka on 2016/5/26.
  */
//class Person{
//  var name :String = ""
//  val age :Int = 0
//  private var address :String = ""
//  private[this] var hobby : String = ""
//}
//
//// scala里面没有提供JAVA的 public static String name
//
//object Person{
//  private var eyeNum = 2
//  println("this is object!")
//  def getEyeNum = eyeNum
//}

// 伴生对象 和 伴生类 要必须放在一个.scala文件中
// 他们互相可以访问private field

//object Person{
//  private val eyeNum = 2
//  def getEyeNum = eyeNum
//}
//
//class Person(val name:String,val age :Int){
//  def sayHello = {
//    println("hi, " + name + " , your age is " + age + "usually you must have " + Person.eyeNum+" eyes.")
//  }
//}

// 抽象类
//abstract class Hello(var m :String){
//  def sayHello(name :String) : Unit
//}
//
//object HelloImpl extends Hello("hello"){
//
//  def sayHello(name :String): Unit ={
//    println(m + "," + name)
//  }
//}
//
//object NiHaoImpl extends Hello("nihao"){
//  def sayHello(name :String): Unit ={
//    println(m + "," + name)
//  }
//}

// apply 通常在伴生对象里面来实现apply方法 Array(1,2,3)
//class Person(val name:String){
//
//}
//object Person{
//  def apply(name:String) = new Person(name)
//}

// Enum 枚举类型, object可以做到
//object  Season extends Enumeration{
//  val SPRING, SUMMER, AUTUMN, WINTER = Value
//}

//object Season extends Enumeration{
//  val SRPING = Value(0,"spring")
//  val SUMMER = Value(1,"summer")
//  val AUTUMN = Value(2,"autumn")
//  val WINTER = Value(3,"winter")
//}

object ObjectObject {
  def main(args: Array[String]) {

//    println(Season(1))
//    println(Season.withName("summer"))
//    for (elem <- Season.values){
//      println(elem)
//    }

//    println(Season.SPRING)

//    val person = Person("yasaka")
//    println(person.name)
//    val arr = Array(1,2,3)

//    HelloImpl.sayHello("yasaka")
//    NiHaoImpl.sayHello("xuruyun")

//    val person = new Person("yasaka",30)
//    person.sayHello

//    println(Person.getEyeNum)
//    println(Person.getEyeNum)
  }
}

