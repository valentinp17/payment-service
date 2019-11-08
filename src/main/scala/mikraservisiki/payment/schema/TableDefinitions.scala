package mikraservisiki.payment.schema

import java.sql.Timestamp

import mikraservisiki.payment.HasDbConfigProvider

object TableDefinitions {

  trait OrdersTable {
    self: HasDbConfigProvider =>

    import profile.api._

    val orders: TableQuery[Orders] = TableQuery[Orders]

    class Orders(tag: Tag) extends Table[Order](tag, "orders"){
      def * = (id, paymentAmount, date, card, status) <> (Order.tupled, Order.unapply)

      def id = column[Long]("id", O.PrimaryKey)

      def paymentAmount = column[BigDecimal]("payment_amount")

      def date = column[Timestamp]("date")

      def card = column[String]("card")

      def status = column[String]("status")
    }
  }

  case class Order(id: Long, paymentAmount: BigDecimal, date: Timestamp, card: String, status: String)
}



