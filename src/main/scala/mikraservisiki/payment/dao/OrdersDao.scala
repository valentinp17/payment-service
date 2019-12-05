package mikraservisiki.payment.dao

import java.sql.Timestamp
import java.time.LocalDateTime

import mikraservisiki.payment.HasDbConfigProvider
import mikraservisiki.payment.schema.TableDefinitions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OrdersDao {
  def addOrder(order: Order): Future[Boolean]
  def markNotificationsSent(orderId: Long): Future[Boolean]
}

object RelationalOrdersDao extends OrdersDao
  with HasDbConfigProvider
  with OrdersTable {

  import profile.api._

  override def addOrder(order: Order): Future[Boolean] = db.run {
    (orders += order) map (_ == 1)
  }

  override def markNotificationsSent(orderId: Long): Future[Boolean] = db.run {
    orders.filter(_.id === orderId).map(_.notificationsSent).update(true).map(_ == 1)
  }
}