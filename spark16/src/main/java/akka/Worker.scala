package akka

import java.util.UUID
import akka.actor.{Props, ActorSystem, ActorSelection, Actor}
import akka.actor.Actor.Receive
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class Worker extends Actor{
  var master : ActorSelection = _
  val workerId = UUID.randomUUID().toString
  import context.dispatcher
  override def preStart(): Unit = {
    master = context.system.actorSelection("akka.tcp://mastersystem@192.168.78.30:8888/user/master")
    master ! "connect"
    master ! RegisterWorkerMessage(workerId,"192.168.78.30",2048,2)
  }

  override def receive: Actor.Receive = {
    case "reply" => {
      println("master reply")
    }
    case RegisteredWorkerMessage(masterUrl) => {
      println("registered master " + masterUrl)
      context.system.scheduler.schedule(0 millis,10000 millis,self,SendHeartBeatMessage)
    }
    case SendHeartBeatMessage =>{
      println("worker send heartbeat")
      master ! HeartBeatMessage(workerId)
    }
  }
}
object Worker {
  def main(args: Array[String]) {
    val configStr =   s"""
                         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
                         |akka.remote.netty.tcp.hostname = "192.168.78.30"
                         |akka.remote.netty.tcp.port = "8899"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem.create("workersystem",config)
    actorSystem.actorOf(Props[Worker],"worker")
    actorSystem.awaitTermination()
  }
}
