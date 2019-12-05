package mikraservisiki.payment.schema

import java.sql.Timestamp
import java.time.LocalDateTime

import mikraservisiki.payment.HasDbConfigProvider

object TableDefinitions {

  trait OrdersTable {
    self: HasDbConfigProvider =>

    import profile.api._

    val orders: TableQuery[Orders] = TableQuery[Orders]

    class Orders(tag: Tag) extends Table[Order](tag, "orders"){
      def * = (id, paymentAmount, card, status, date, notificationsSent) <> (Order.tupled, Order.unapply)

      def id = column[Long]("id", O.PrimaryKey)

      def paymentAmount = column[BigDecimal]("payment_amount")

      def date = column[Timestamp]("date")

      def card = column[String]("card")

      def status = column[String]("status")

      def notificationsSent = column[Boolean]("notifications_sent")
    }
  }

  case class Order(id: Long, paymentAmount: BigDecimal, card: String, status: String, date: Timestamp = Timestamp.valueOf(LocalDateTime.now), notificationSent: Boolean = false)
}



