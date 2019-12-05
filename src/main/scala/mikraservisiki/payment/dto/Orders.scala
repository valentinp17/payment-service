package mikraservisiki.payment.dto

import akka.http.scaladsl.model.DateTime
import play.api.libs.json.{Format, Json, OFormat}

object Orders {

  case class OrderDto(id: Long)

  case class QueueOrderDto(orderId: Long, status: Option[String] = None)

  implicit val orderDtoFormat: OFormat[OrderDto] = Json.format[OrderDto]

  implicit val queueOrderDtoFormat: OFormat[QueueOrderDto] = Json.format[QueueOrderDto]
}
