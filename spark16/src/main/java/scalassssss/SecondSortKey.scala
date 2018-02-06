package scalassssss

/**
  * Created by root on 2016/6/20.
  */
class SecondSortKey(val first:Int, val second : Int) extends Serializable with Ordered[SecondSortKey]{

  def compare(that : SecondSortKey): Int ={
    if(this.first - that.first != 0){
      this.first - that.first
    }else{
      this.second - that.second
    }
  }
}
