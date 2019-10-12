package mikraservisiki.orders.dto

import akka.http.scaladsl.model.DateTime
import play.api.libs.json.{Format, Json}

object Orders {

  case class OrderDto(id: Long)

  implicit val orderDtoFormat: Format[OrderDto] = Json.format[OrderDto]
}
