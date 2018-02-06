package sca

/**
  * Created by shengjk1 on 2016/8/11.
  */
object TestMapTuple {
  def main(args: Array[String]): Unit = {
    //不可变的映射，内部元素的值也不可改变
    val scores = Map("Alice" -> 32, "A" -> 10)

    import scala.collection.mutable.HashMap
    val scores1 = HashMap("Alice" -> 32, "A" -> 10)
    println(scores1("A"))
    println(scores1.getOrElseUpdate("B", 11))
    println(scores1.getOrElse("B", 0))
    //更新或者新加
    scores1("A")=100
    scores1("C")=100
    //
    scores1+=("C"->121)
    println(scores1)
    scores1("A") = 100
    scores1 += ("B" -> 111)
    scores1-=("B")

    //不可变的所有操作都会产生一个新的
    val scoresa=scores+("A"->111)
    println(scores1 +"\n"+scoresa)


    //迭代
    for((k,v)<-scores){
      println(k +" "+v)
    }
    println(scores.keySet)
    println(scores.keys)
    for(v<-scores.values){
      print(v+" ")
    }

    for((k,v)<-scores) yield (v,k)

    val scoress=scala.collection.immutable.SortedMap("A"->12
    ,"C"->12,"0"->21)
    println(scoress)

    //tuple
    val t=(1,3,"123","1234")
    val second=t._2
    println(second)

    val (a,c,b,_) =t
    println("New York".partition(_.isUpper))
  }

}
