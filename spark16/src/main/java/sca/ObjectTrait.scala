package sca

import scala.collection.script.Message

/**
  * Created by yasaka on 2016/5/31.
  */

// Trait 类 对象 继承 java单继承 实现多个接口！ java8提供了lambda,接口允许提供一些实现
// scala也一样,不支持多继承！但是scala允许你扩展！继承多个Trait,你理解的它的时候可以首先
// 理解为接口, scala trait 可以有具体的方法和Field

// trait 作为接口来使用 extends 不要override

//trait HelloTrait{
//  def sayHello(name:String)
//}
//
//trait MakeFriendsTrait{
//  def makeFriends(p:Person)
//}
//多个trait不能有相同的同类方法名，
//class Person(val name:String) extends HelloTrait with MakeFriendsTrait{
//  def sayHello(name:String) = {println("hello, "+name)}
//  def makeFriends(p:Person) = println("hello, my name is "+ name + " , your name is "+p.name)
//}

// Scala中的trait还可以有具体方法！
//trait Logger{
//  def log(message: String) = println(message)
//}
//
//class Person(val name:String) extends Logger{
//  def makeFriends(p:Person): Unit ={
//    println("hello, my name is "+ name + " , your name is "+p.name)
//    log("makeFridends method is invoked with parameter Person[name="+p.name+"]")
//  }
//}

// Trait还可以定义具体的field，这种方法类里面获取到的Field和继承父类里面的field是不同的！！！
//trait里的file等价于该类自己的属性
//trait Person{
//  val eyeNum : Int = 2
//}
//
//class Student(val name:String) extends Person{
//  def sayHello = println("nihao, i am "+ name + " i have "+eyeNum+" eyes.")
//}

// 抽象Field，子类中也必须被实现
//trait SayHello{
//  val msg : String
//  def sayHello(name:String) = println(msg + ","+ name)
//}
//
//class Person(val name:String) extends SayHello{
//  val msg : String = "nihao"
//  def makeFridends(p:Person): Unit ={
//    sayHello(p.name)
//    println("i am "+ name + ", i want to make friends with you !")
//  }
//}

// Trait最牛之处！我们可以给类的每个实例对象让它去继承不同的Trait

//trait Logged {
//  def log(msg:String) {}
//}
//
//trait Eyes{
//  val eyeNum : Int = 2
//}
//
//trait MyLogger extends Logged{
//  override def log(msg:String): Unit = {
//    println("log: " + msg)
//  }
//}
//跟类trait的调用顺序不太一样
//class Person(val name:String) extends Logged{
//  def sayHello: Unit ={
//    println("hi, i am "+name)
//    log("sayHello is invoked!")
//  }
//}

// Trait调用链也是高级功能！可以直接实现我们设计模式：责任链设计模式！
// 说白了就是让相似的对象依次调用同一个方法！然后把他们组成一个调用链条！
// super
// 类中调用多个Trait中都有的方法时，会从最右边的trait方法开始执行！
//trait  Handler{
//  def handle(data:String){}
//}
//
//trait DataValidHandler extends Handler{
//  override def handle(data:String): Unit ={
//    println("check data : "+ data)
//    super.handle(data)
//  }
//}
//
//trait SignatureValidHandler extends Handler{
//  override def handle(data:String): Unit ={
//    println("check signature: "+data)
//    super.handle(data)
//  }
//}
//
//class Person(val name:String) extends SignatureValidHandler with DataValidHandler{
//  def sayHello = {
//    println("hello + "+name)
//    handle(name)
//  }
//}

// 子Trait可以重写父Trait里面的抽象方法
//trait Logger {
//  def log(msg:String)
//}
//
//trait MyLogger extends Logger{
//  abstract  override def log(msg:String) {super.log(msg)}
//}

// 混合使用Trait的具体方法和抽象方法
// 设计模式中的模板设计模式！！！！说白了什么是这个设计模式？
// 就是可以有公共的方法，然后也可以有抽象方法，然后具体的类去实现！

//trait  Valid{
//  def getName:String
//  def valid:Boolean = {
//    getName == "yasaka"
//  }
//}
//
//class  Person(val name:String) extends  Valid{
//  println(valid)
//  def getName = name
//}

// 构造机制！！！
//trait Logger{
//  println("Logger's constructor!")
//}
//trait MyLogger extends Logger{
//  println("MyLogger's constructor!")
//}
//trait TimeLogger extends Logger{
//  println("TimeLogger's constructor!")
//}
//class  Person{
//  println("Person's constructor!")
//}
//class Student extends Person with MyLogger with TimeLogger{
//  println("Student's constructor!")
//}

// Trait和类的区别是不能被实例化！高级功能--提前定义！
//trait SayHello{
//  val msg:String
//  println(msg.toString)
//}
//class Person

// 1, 动态混入的方式给它先提前初始化之前先重写！
// 2, 定义Class
//class Person extends {
//  val msg:String = "init"
//} with SayHello {}

// 3,lazy value
//trait SayHello{
//  lazy val msg: String = null
//  println(msg.toString)
//}
//class Person extends SayHello{
//  override lazy val msg:String = "init"
//}

// trait继承类！！！
//class MyUtil{
//  def printMessage(msg:String) = println(msg)
//}
//
//trait Logger extends  MyUtil{
//  def log(msg:String) = printMessage("log: "+msg)
//}
//
//class Person(val name:String) extends  Logger{
//  def sayHello: Unit ={
//    log("hi, i am "+ name)
//    printMessage("hi, i am "+name)
//  }
//}

object ObjectTrait {
  def main(args: Array[String]) {
//    val person = new Person("yasaka")
//    person.sayHello

//    val person = new Person

//    val person = new {
//      val msg:String = "init"
//    }with Person with SayHello

//    val stu = new Student

//    val stu = new Person("yasaka")
//    println(stu.getName)

//    val person = new Person("yasaka")
//    person.sayHello

//    val person1 = new Person("yasaka")
//    person1.sayHello
//    val person2 = new Person("xuruyun") with MyLogger with Eyes
//    person2.sayHello
//    println(person2.eyeNum)

//    val person1 = new Person("yasaka")
//    val person2 = new Person("xuruyun")
//    person1.makeFridends(person2)

//    val stu = new Student("yasaka")
//    stu.sayHello

//    val person = new Person("yasaka")
//    val girl = new Person("xuruyun")
//    person.makeFriends(girl)

//    val person1 = new Person("yasaka")
//    val person2 = new Person("xuruyun")
//    person1.sayHello("xuruyun")
//    person1.makeFriends(person2)
  }
}
