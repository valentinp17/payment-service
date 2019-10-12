package mikraservisiki.payment.handler

import mikraservisiki.payment.dao.OrdersDao
import mikraservisiki.payment.dto.Orders.OrderDto
import mikraservisiki.payment.schema.TableDefinitions.Order

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PaymentService {
  def performPayment(orderId: Long, username: String, cardAuthorizationInfo: String): Future[Option[OrderDto]]
}

class PaymentServiceImpl(
                        orderDao: OrdersDao
                      ) extends PaymentService {


  override def performPayment(orderId: Long, username: String, cardAuthorizationInfo: String): Future[Option[OrderDto]] = {
    cardAuthorizationInfo match {
      case "AUTHORIZED" => orderDao.addOrder(orderId, username, cardAuthorizationInfo).map(order => Some(OrderDto(order.id)))
      case _ => Future.successful(None)
    }
  }
}
