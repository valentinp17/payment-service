package mikraservisiki.payment.queues

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.spingo.op_rabbit.PlayJsonSupport._
import com.spingo.op_rabbit.{Message, RabbitControl, _}
import mikraservisiki.payment.dto.Orders._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

trait OrdersQueueService {
  def postOrderFailed(order: QueueOrderDto): Future[Unit]

  def postOrderStatusUpdate(order: QueueOrderDto): Future[Unit]
}

class RabbitOrdersQueueService(implicit actorSystem: ActorSystem) extends OrdersQueueService {
  implicit val rabbitErrorLogging = Slf4jLogger
  val rabbitControl: ActorRef = actorSystem.actorOf(Props[RabbitControl])

  implicit private val timeout = Timeout(5 seconds)

  override def postOrderFailed(order: QueueOrderDto): Future[Unit] = {
    rabbitControl ? Message.exchange(order, "order-failed")
    }.map(_ => {})

  override def postOrderStatusUpdate(order: QueueOrderDto): Future[Unit] = {
    rabbitControl ? Message.exchange(order, "order-status-update")
    }.map(_ => {})
}