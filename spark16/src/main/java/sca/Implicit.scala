package sca

/**
  * Created by jk on 2016/7/18.
  */

/*
隐式转换，相当于extends
 */
class SpecialPerson(val name: String)


class Student(val name: String)

class Older(val name: String)

class Teacher(val name: String)


object Implicit {
  implicit def object2SpecialPerson(obj: Object): SpecialPerson = {
    if (obj.getClass == classOf[Student]) {
      new SpecialPerson(obj.asInstanceOf[Student].name)
    } else if (obj.getClass == classOf[Older]) {
      new SpecialPerson(obj.asInstanceOf[Older].name)
    } else {
      Nil
    }
  }

  var ticketNumber = 0

  def buySpecialTicket(p: SpecialPerson) = {
    ticketNumber += 1
    "current total tickets: " + ticketNumber
  }


  def main(args: Array[String]) {
    val huwei = new Student("huwei")
    val haimingwei = new Older("haimingwei")
    val yasaka = new Teacher("yasaka")

    println(buySpecialTicket(huwei))
    println(buySpecialTicket(haimingwei))
    println(buySpecialTicket(yasaka))
  }

}
