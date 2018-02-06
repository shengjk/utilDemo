package akka

import akka.actor.{Props, ActorSystem, Actor}
import akka.actor.Actor.Receive
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.collection.mutable

class Master extends Actor{

  var idToWorker = new mutable.HashMap[String,WorkerInfo]()
  var workers = new mutable.HashSet[WorkerInfo]()
  import context.dispatcher
  override def preStart(): Unit = {
    println("master preStart")
    context.system.scheduler.schedule(0 millis,5000 millis, self, CheckWorkerTimeoutMessage)
  }
  override def receive: Receive = {
    case "connect" => {
      println("a client connect")
      sender ! "reply"
    }
    case RegisterWorkerMessage(id,workHost,memory,cores) => {
      if(!idToWorker.contains(id)){
        val worker = new WorkerInfo(id,workHost,memory,cores)
        workers.add(worker)
        idToWorker(id) = worker
        sender ! RegisteredWorkerMessage("akka.tcp://mastersystem@192.168.78.30:8888")
      }
    }
    case HeartBeatMessage(workId) => {
      val worker = idToWorker(workId)
      worker.lastHeartbeat = System.currentTimeMillis()
    }

    case CheckWorkerTimeoutMessage => {
      val current = System.currentTimeMillis()
      val toRemoveWorkers = workers.filter( w => current - w.lastHeartbeat > 20000 ).toArray
      for(worker <- toRemoveWorkers){
        workers -= worker
        idToWorker.remove(worker.id)
      }
      println("workers size " + workers.size)
    }
  }
}
object Master {
  def main(args: Array[String]) {
    val configStr =   s"""
               |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
               |akka.remote.netty.tcp.hostname = "192.168.78.30"
               |akka.remote.netty.tcp.port = "8888"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem.create("mastersystem",config)
    actorSystem.actorOf(Props[Master],"master")
    actorSystem.awaitTermination()
  }
}
