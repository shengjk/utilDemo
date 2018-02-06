package sca

/**
  * Created by shengjk1 on 2016/8/11.
  */
object TestArray {
  def main(args: Array[String]): Unit = {
    import scala.collection.mutable.ArrayBuffer
    val a =ArrayBuffer(1,2,3,4,5,6,8,7)
    a.filter(_%2==0).map(2*_).foreach(print(_))

    a+=1
    a+=(0,0,0)
    a++=Array(8,13,12)
    a++=ArrayBuffer(10,10,11)
    a.remove(2,10)
    //从1开始
    a-=(2)
    //从0开始
//    a.insert(0,20,30)
    // ArrayBuffer(1, 2, 8, 13, 12, 10, 10, 11)
    print("\n "+a)

  }

}
