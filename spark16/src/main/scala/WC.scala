import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/6/29.
  */
object WC {

  def main(args: Array[String]) {
     val conf =new SparkConf().setAppName("WC").setMaster("local[4]")
    val sc =new SparkContext(conf)
//    sc.parallelize

    val lines=sc.textFile("/Users/iss/Desktop/1111")
    lines.flatMap(x =>x.split(","))
            .map(x =>(x,1))
            .reduceByKey((x,y)=>x+y).
            foreach(println(_))

    sc.stop()


  }

}
