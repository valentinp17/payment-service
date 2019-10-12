package mikraservisiki.orders.dao

import java.sql.Timestamp
import java.time.LocalDateTime

import mikraservisiki.orders.HasDbConfigProvider
import mikraservisiki.orders.schema.TableDefinitions._

import scala.concurrent.Future

trait OrdersDao {
  def addOrder(orderId: Long, username: String, cardAuthorizationInfo: String): Future[Order]
}

object RelationalOrdersDao extends OrdersDao
  with HasDbConfigProvider
  with OrdersTable {

  import profile.api._

  override def addOrder(orderId: Long, username: String, cardAuthorizationInfo: String): Future[Order] = db.run {
    (orders returning orders) += Order(orderId, 500,
      Timestamp.valueOf(LocalDateTime.now()), "1234567812345678")
  }

}