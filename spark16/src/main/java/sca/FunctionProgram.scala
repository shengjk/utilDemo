package sca

/**
  * Created by yasaka on 2016/5/25.
  */



object FunctionProgram {

  def main(args: Array[String]) {
    // Java 只能面向对象,说白了面向对象呢就是把对象传来传去
    // scala 可以面向函数,说白了面向函数呢就是把函数传来传去
    // method方法这个词通常指的是类里面的方法
    // function通常指的就是外面的单独定义的函数

    // 函数名、参数、函数体/方法体、返回类型
    //    def sayHello(name : String, age:Int) = {
    //      if(age > 18) {printf("hi %s, you are a big boy\n", name); age}
    //      else {printf("hi %s, you are a little boy\n", name); age}
    //    }
    //    println(sayHello("yasaka",30))


    // java 中这个一等公民是类和对象，方法
    // scala 函数的概念 中这个一等公民是类和对象还是函数 函数可以独立存在
    // java 技术生态 Spring Lucene Hadoop

    // scala语法规定函数赋值给变量时候，必须在函数后面加上空格和下划线，否则报错！
    //    def sayHello(name:String)={
    //      println("hello, "+name)
    //    }
    //将函数赋值给参数
    //    val sayHelloFunc = sayHello _
    //    sayHelloFunc("yasaka")

    // 匿名函数
//    val sayHelloFunc = (name:String) => println("hello ," + name)
//    sayHelloFunc("yasaka")

    // 函数作为参数传递 scala高阶函数high order function
//    def sayHelloFuncName(name:String, age:Int) = {
//      println("hello ," + name); 300
//    }
//    val sayHelloFunc = (name:String,age:Int) => {println("hello ," + name); 300}
//    def greeting(func: (String,Int)=>Int, name:String, age:Int): Unit ={
//        func(name,age)
//    }
//    greeting(sayHelloFuncName,"yasaka",30)

    // 小作业，java用匿名内部类来实现类似的功能！

//    val arr = Array(1,2,3,4,5,6,7,8,9,10).map((num:Int)=> num * 2)
//    println(arr.mkString(","))

    // 高阶函数可以自动推断类型
//    def greeting(func: String=>Unit, name:String): Unit ={
//      func(name)
//    }
//    greeting((name:String) => println("hello, "+name),"yasaka")
//    greeting(name => println("hello, "+name),"yasaka")

//    def triple(func : Int => Int) ={
    //      func(3)
    //    }
    //    println(triple(a=>a*10))
    //    println(triple(_*10))

    // scala里面常用的高阶函数
    // map
//    val arr = Array(1,2,3,4,5,6,7,8,9,10).map((num:Int)=> num * 2)
    //    println(arr.mkString(","))
    // foreach
//    (1 to 10).map("*" * _).foreach(println(_))
    // filter
//    (1 to 10)   .filter(_ % 2 == 0).foreach(print _)
    // reduceLeft 1,2,3,4,5,6,7,8,9,10
//   println( (1 to 10)   .reduce(_ - _))

    // sortWith
//    Array(2,4,8,4,2).sortWith(_ > _).foreach(println _)

    // 闭包 函数在变量不处于其有效作用域时候，还能够对变量进行访问，称为闭包
//    def getGreetingFunc(msg:String): String=>Unit = {
//      (name:String) => println(msg + "," + name)
//    }
//    val greetingFuncHello = getGreetingFunc("hello")
//    val greetingFuncNihao = getGreetingFunc("nihao")
//    greetingFuncHello("yasaka")
//    greetingFuncNihao("yasaka")

    // java SAM single abstract method
    // 使用SAM需要隐式转换！！！
//    import javax.swing._
//    import java.awt.event._
//    val button = new JButton("click")
//
//    implicit def getActionListener(actionProcessFunc: (ActionEvent)=>Unit)=new ActionListener {
//      override def actionPerformed(e: ActionEvent): Unit = actionProcessFunc(e)
//    }
//    button.addActionListener((event:ActionEvent) => println("Click!!!"))

    // curring 函数
//    def sum(a:Int, b:Int) = a+b
//    sum(1,1)
//    def sum2(a:Int) = (b:Int) => a+b
//    sum2(1)(1)
//    def sum3(a:Int)(b:Int) = a+b
//    sum3(1)(1)

    // return
//    def greeting(name:String)={
//      def sayHello(name:String)={
//        "hello " + name
//      }
//      sayHello _
//    }
//    println(greeting("sayHello")("yasaka"))

    val sayHello = (name: String, age : Int) => {
      println(name)
      val i : Int = 0
      if(age > 0) {
         i
      }
      else {
         "str"
      }
    }
    val x = sayHello("xiaocai",30)
    print(x)
  }



}

