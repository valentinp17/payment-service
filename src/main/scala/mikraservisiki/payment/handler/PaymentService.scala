package mikraservisiki.payment.handler

import java.util.UUID

import mikraservisiki.payment.dao.OrdersDao
import mikraservisiki.payment.dto.Orders.OrderDto
import mikraservisiki.payment.handler.payments.CardAuthorizations._
import mikraservisiki.payment.handler.payments.{AuthorizationFailedException, PaymentStatuses}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

trait PaymentService {
  def performPayment(orderId: Long, amount: BigDecimal, cardAuthorizationInfo: String): Future[OrderDto]
}

class PaymentServiceImpl(
                          orderDao: OrdersDao
                        ) extends PaymentService {


  override def performPayment(orderId: Long, amount: BigDecimal, cardAuthorizationInfo: String): Future[OrderDto] = {
    val statusTry = Try {
      cardAuthorizationInfo match {
        case AUTHORIZED => PaymentStatuses.Success
        case UNAUTHORIZED => PaymentStatuses.Failed
        case _ => throw new IllegalArgumentException("unknown card type")
      }
    }

    for (
      status <- Future.fromTry(statusTry);
      dbOperationIsSuccessful <- orderDao.addOrder(orderId, amount, UUID.randomUUID().toString, status.toString)
    ) yield {
      if (!dbOperationIsSuccessful)
        throw new RuntimeException("failed to add info to db")
      if (status == PaymentStatuses.Failed)
        throw AuthorizationFailedException
      OrderDto(orderId)
    }
  }
}
