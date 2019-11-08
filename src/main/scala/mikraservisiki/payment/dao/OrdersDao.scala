package mikraservisiki.payment.dao

import java.sql.Timestamp
import java.time.LocalDateTime

import mikraservisiki.payment.HasDbConfigProvider
import mikraservisiki.payment.schema.TableDefinitions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OrdersDao {
  def addOrder(orderId: Long, paymentAmount: BigDecimal, card: String, status: String): Future[Boolean]
}

object RelationalOrdersDao extends OrdersDao
  with HasDbConfigProvider
  with OrdersTable {

  import profile.api._

  override def addOrder(orderId: Long, paymentAmount: BigDecimal, card: String, status: String): Future[Boolean] = db.run {
    orders += Order(orderId, paymentAmount, Timestamp.valueOf(LocalDateTime.now), card, status)
  } map (_ == 1)

}