package sca

/**
  * Created by yasaka on 2016/5/27.
  */

// 继承：其实继承的好处就是代码的复用！可以有更好的可维护性和可扩展性
// extends field method
// 如果父类不想被继承 final

//class Person{
//  private var name = "yasaka"
//  def getName = name
//}
//
//class Student extends Person{
//  private var score = "100"
//  def getScore = score
//}

// override super
//class Person{
//  private var name = "yasaka"
//  def getName = name
//}
//
//class Student extends Person{
//  private var score = "100"
//  def getScore = score
//  override def getName = "Hi, i am a student " + super.getName
//}

// 子类覆盖父类里面的　field
//class Person{
//  val name :String = "Person"
//  def age : Int = 0
//}
//
//class Student extends  Person{
//  override val name :String = "yasaka"
//  override val age : Int = 30
//}

//class Person
//class Student extends Person

// isInstanceOf true false
// asInstanceOf
// isInstanceOf只能判断出对象是否是指定类以及其子类

// getClass classOf[类] 如果要精准的判断对象就是指定类的对象

// 模式匹配 来判断代码的类可维护性和可扩展高！匹配第一个就停了,不能精确匹配


// protected java一样, scala同样可以使用关键字protected,
// 这样子类就不需要super关键字, 直接就可以访问field method
// protected[this] 则只能在当前子类对象中访问父类的field method

//class Person{
//  protected var name : String = "yasaka"
//  protected[this] var hobby : String = "football"
//}
//
//class Student extends  Person{
//  def sayHello = println("hi, "+name)
//  def makeFriends(stu :Student): Unit ={
//    println("my hobby is " + hobby + "your hobby is "+stu.hobby)
//  }
//}

// 下面讲的这个关于constructor的内容也是比较重要的！
// 不加override val会调用父类的构造函数！！！否则加上override val是子类要覆盖父类的field

//class Person(val name:String , val age:Int ){
//  println("this is Person constructor " + name)
//
//}
//class Student(name:String, age:Int , var score :Double) extends Person(name, age){
//  println("this is Student constructor " + name)
//
//  def this(name:String){
//    this(name,0,0)
//  }
//  def this(age:Int){
//    this("yasaka", age, 0)
//  }
//}

class Person(val name:String, val age:Int)
class Student(codename:String , override val age:Int , var score :Double) extends Person(codename, age){
  override val name : String = "secret"

  def this(name:String){
    this(name,0,0)
  }
  def this(age:Int){
    this("yasaka", age, 0)
  }
}


// 匿名内部类
//class Person(val name:String){
//  def sayHello = println("hello , " + name)
//}

// 抽象类
//abstract class Person(val name:String){
//  val hobby:String
//  def sayHello : Unit
//}
//
//class Student(name:String) extends Person(name){
//  val hobby :String = "football"
//  def sayHello:Unit = println("hello, "+name)
//}

object ObjectExtends {

  def main(args: Array[String]) {

//    val p = new Person("yasaka")
//    val stu = new Student("yasaka")
//    stu.hobby

//    val p = new Person("yasaka"){
//      override def sayHello =println( "hi, " +name)
//    }
////    p.sayHello
//
//    def greeting (person: Person { def sayHello :Unit}){
//        person.sayHello
//    }
//    greeting(p)

    val stu = new Student("yasaka",30,100)
    println(stu.name)

//    val person : Person = new Student
//    person match {
//      case p:Student => println("okay type match student")
//      case p:Person => println("okay type match person")
//      case _ => println("unknown type")
//    }

//    val person : Person = new Student
//    println(person.isInstanceOf[Person])
//    println(person.getClass == classOf[Person])
//    println(person.getClass == classOf[Student])

//    val person : Person = new Student
//    var stu:Student = null
//    println(stu.isInstanceOf[Student])
//    if(person.isInstanceOf[Student]) stu = person.asInstanceOf[Student]
//    println(stu)


//    val person = new Person
//    println(person.name)
//    val stu = new Student
//    println(stu.name)

//    val stu = new Student
//    println(stu.getName)

    // var val private private[this]

//    val stu = new Student
//    println(stu.getName)
//    println(stu.getScore)
  }
}
