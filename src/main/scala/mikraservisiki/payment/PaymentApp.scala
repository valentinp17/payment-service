package mikraservisiki.payment

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import mikraservisiki.payment.dao.RelationalOrdersDao
import mikraservisiki.payment.handler.PaymentServiceImpl
import mikraservisiki.payment.queues.RabbitOrdersQueueService
import mikraservisiki.payment.routing.AppRouting

import scala.concurrent.ExecutionContextExecutor

object PaymentApp extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)
  val queueService = new RabbitOrdersQueueService()
  val paymentService = new PaymentServiceImpl(RelationalOrdersDao, queueService)
  val routes = AppRouting.route(paymentService)
  Http().bindAndHandle(
    routes, config.getString("http.interface"), config.getInt("http.port")
  )
}