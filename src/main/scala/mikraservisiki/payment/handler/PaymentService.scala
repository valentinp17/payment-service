package mikraservisiki.payment.handler

import java.util.UUID

import com.spingo.op_rabbit.Message
import mikraservisiki.payment.dao.OrdersDao
import mikraservisiki.payment.dto.Orders.{OrderDto, QueueOrderDto}
import mikraservisiki.payment.handler.payments.CardAuthorizations._
import mikraservisiki.payment.handler.payments.PaymentStatuses.{Failed, Paid}
import mikraservisiki.payment.handler.payments.{AuthorizationFailedException, PaymentStatuses}
import mikraservisiki.payment.queues.OrdersQueueService
import mikraservisiki.payment.schema.TableDefinitions.Order

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

trait PaymentService {
  def performPayment(orderId: Long, amount: BigDecimal, cardAuthorizationInfo: String): Future[OrderDto]
}

class PaymentServiceImpl(
                          orderDao: OrdersDao,
                          ordersQueueService: OrdersQueueService
                        ) extends PaymentService {


  override def performPayment(orderId: Long, amount: BigDecimal, cardAuthorizationInfo: String): Future[OrderDto] = {
    val statusTry = Try {
      cardAuthorizationInfo match {
        case AUTHORIZED => Paid
        case UNAUTHORIZED => Failed
        case _ => throw new IllegalArgumentException("unknown card type")
      }
    }

    for (
      status <- Future.fromTry(statusTry);
      dbInsertIsSuccessful <- orderDao.addOrder(Order(orderId, amount, UUID.randomUUID().toString, status.toString));
      _ <- ordersQueueService.postOrderStatusUpdate(QueueOrderDto(orderId, Some(status.toString)));
      _ <- status match {
        case Failed => ordersQueueService.postOrderFailed(QueueOrderDto(orderId))
        case _ => Future.successful(Unit)
      };
      dbUpdateIsSuccessful <- orderDao.markNotificationsSent(orderId)
    ) yield {
      if (!dbInsertIsSuccessful || !dbUpdateIsSuccessful)
        throw new RuntimeException("failed to add info to db")
      if (status == Failed)
        throw AuthorizationFailedException
      OrderDto(orderId)
    }
  }
}
