package sca

/**
  * Created by yasaka on 2016/6/1.
  */

class Man(val name:String)

class Musician(val name:String){
  def sing = println("sing a wonderful song!")
}

class FootPlayer(val name:String){
  def sing = println("dian le ge qiu!")
}

object Implicit02 {

  implicit def man2musician(man:Man):Musician = new Musician(man.name)

  def main(args: Array[String]) {

    val yasaka = new Man("yasaka")
    yasaka.sing
  }
}
