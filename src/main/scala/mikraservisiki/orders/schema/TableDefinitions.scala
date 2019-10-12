package mikraservisiki.orders.schema

import java.sql.Timestamp

import mikraservisiki.orders.HasDbConfigProvider

object TableDefinitions {

  trait OrdersTable {
    self: HasDbConfigProvider =>

    import profile.api._

    val orders: TableQuery[Orders] = TableQuery[Orders]

    class Orders(tag: Tag) extends Table[Order](tag, "orders"){
      def * = (id, paymentAmount, date, card) <> (Order.tupled, Order.unapply)

      def id = column[Long]("id", O.PrimaryKey)

      def paymentAmount = column[BigDecimal]("payment_amount")

      def date = column[Timestamp]("date")

      def card = column[String]("card")
    }
  }

  case class Order(id: Long, paymentAmount: BigDecimal, date: Timestamp, card: String)
}



