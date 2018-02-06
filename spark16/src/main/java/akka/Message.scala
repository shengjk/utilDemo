package akka

trait Message extends Serializable

case class RegisterWorkerMessage(val id: String ,val workHost: String ,val memory : Int, val cpuCores : Int) extends Message
case class RegisteredWorkerMessage(val masterUrl:String) extends  Message
case object SendHeartBeatMessage extends Message
case object CheckWorkerTimeoutMessage extends Message
case class HeartBeatMessage(val id : String) extends  Message