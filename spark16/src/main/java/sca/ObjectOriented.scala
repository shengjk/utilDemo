package sca

/**
  * Created by yasaka on 2016/5/26.
  */
class HelloWorld {

  private var name = "yasaka"
  def sayHello() {println("hello, " + name)}
  def getName = name
}

// 面向对象编程 封装、继承、多态
// 1, var 自动提供getter和setter方法,我们不需要自己去写

//class Student {
//  var name = "yasaka"
//}

//get set方法跟普通方法没有什么不一样的，还是安照普通方法那样调用
//跟java一样就是一个名字而已。
// 2, private 修饰 field 那么getter 和 setter也是private的

//class Student {
//    private var name = "yasaka"
//    def myName = "your name is " + name
//    def myName_=(newName:String): Unit ={
////      println("your new name is " + newName)
//      name = newName
//    }
//}

//class Student{
//  private var name = "yasaka"
//
//  def updateName(newName :String): Unit ={
//      name = newName
//  }
//
//  def getName_=(newName:String): Unit ={
//    name = newName
//  }
//  def getName = "your name is + " + name
//}

//class Student{
//  private var myAge = 0
//  def age_=(newAge :Int): Unit ={
//    if(newAge > 0) myAge = newAge
//    else  println("Illegal age!!!")
//  }
//  def age = myAge
//  def older(s : Student): Int ={
//    if(myAge > s.myAge) 1
//    else 0
//  }
//}

//import  scala.reflect.BeanProperty
////class  Student{
////  @BeanProperty var name : String = _
////}
//
//class Student(@BeanProperty var name :String)

// 3, val 只会生成getter方法！

//class Student{
//  val name = "yasaka"
//}

// 4，如果不希望要getter 和 setter方法, 则使用 private[this]

//class Student{
//  private[this] var myAge = 0
//  def age_=(newAge :Int): Unit ={
//    if(newAge > 0) myAge = newAge
//    else  println("Illegal age!!!")
//  }
//  def age = myAge
//  def older(s : Student): Int ={
//    if(myAge > s.myAge) 1
//    else 0
//  }
//}

// 如果现在这个代码是private[this] 那么就会直接报错了

// 构造器

//class Student{
//  private var name = ""
//  private var age = 0
//  def this(name:String){
//    this()
//    this.name = name
//  }
//  def this(name:String, age:Int){
//    this(name)
//    this.age = age
//  }
//}

//class Student(val name:String = "yasaka" , val age:Int = 30){
//  println("your name is " + name + " , your age is "+age)
//}

// 内部类

//import scala.collection.mutable.ArrayBuffer
//class Class{
//  class Student(val name:String){}
//  val students = new ArrayBuffer[Student]()
//  def getStudent(name :String)={
//    new Student(name)
//  }
//}

object ObjectOriented{
  def main(args: Array[String]) {

//    val class1 = new Class
//    val stu1 = class1.getStudent("yasaka")
//    class1.students += stu1
//
//    val class2 = new Class
//    val stu2 = class2.getStudent("yasaka")
//    class1.students += stu2

//    val yasaka = new Student

//    val stu1 = new Student
//    val stu2 = new Student("yasaka")
//    val stu3 = new Student("yasaka",30)


//    val yasaka = new Student
//    println(yasaka.name)
//    yasaka.name = "xuruyun"

//    val yasaka = new Student("yasaka")
//    println(yasaka.getName)
//    yasaka.setName("xuruyun")
//    println(yasaka.getName)

//    val hw = new HelloWorld
//    hw.sayHello()
//    println(hw.getName)

//    val yasaka = new Student
//    println(yasaka.name)
//    yasaka.name = "xuruyun"
//    println(yasaka.name)

//    val yasaka = new Student
//    println(yasaka.myName)
//    yasaka.myName = "xuruyun"
//    println(yasaka.myName)

//    val yasaka = new Student
//    println(yasaka.getName)
////    yasaka.updateName("xuruyun")
//    yasaka.getName = "xuruyun"
//    println(yasaka.getName)

//    val yasaka = new Student
//    yasaka.age = 30
//    val xuruyun = new Student
//    xuruyun.age = 40
//    println(yasaka.older(xuruyun))
  }
}
