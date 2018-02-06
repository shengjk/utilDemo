package scalassssss

/**
  * Created by 小省 on 2016/7/4.
  */
/*
不可变的list会生成一个新的list
list= Integer.valueOf(
              map(x.next())
          ):: list

    不可变的增删改都会产生一个新的
 */
object MapTupleBasicOperation {
  def main(args: Array[String]) {
    //映射  默认额是不可变的
    val scores =scala.collection.mutable.Map("Alice" -> 10,"Bob" -> 2,"CD"->4)
    scores("Bob")=11111

    //如果是不可变的则会生成一个新的  val new =scores +("Bob1" ->12,"Bob2" ->121)
    scores +=("Bob1" ->12,"Bob2" ->121)
    scores -=("Bob")
//
//    println(if (scores.contains("Bob") ) scores("Bob") else 0)
//    println(scores.getOrElse("Bob1","ddddddddddd"))

    //映射遍历
    for ((k,v) <- scores){
      println(k,v)
    }
    scores.keySet
    for(v <- scores.values){
println(v)
    }

    //元组 tuple  数据类型可以不一致
    (1,2,"123")


  }

}
