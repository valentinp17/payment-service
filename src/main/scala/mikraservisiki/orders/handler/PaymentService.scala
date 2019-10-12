package mikraservisiki.orders.handler

import mikraservisiki.orders.dao.OrdersDao
import mikraservisiki.orders.dto.Orders.OrderDto
import mikraservisiki.orders.schema.TableDefinitions.Order

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
      case "AUTHORIZED" => orderDao.addOrder(orderId, username, cardAuthorizationInfo).flatMap(order =>
      OrderDto(
      case Some(Order) => OrderDto(Order.)
      ))
      case _ => Future(None)
    }
  }
}
