package akka

class WorkerInfo (val id: String ,val workHost: String ,val memory : Int, val cpuCores : Int) {
  var lastHeartbeat : Long = _
}
