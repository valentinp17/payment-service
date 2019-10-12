package mikraservisiki.orders

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import mikraservisiki.orders.dao.RelationalOrdersDao
import mikraservisiki.orders.handler.PaymentServiceImpl
import mikraservisiki.orders.routing.AppRouting

import scala.concurrent.ExecutionContextExecutor

object OrderApp extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)
  val orderService = new PaymentServiceImpl(RelationalOrdersDao)
  val routes = AppRouting.route(orderService)
  Http().bindAndHandle(
    routes, config.getString("http.interface"), config.getInt("http.port")
  )
}