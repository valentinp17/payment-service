package mikraservisiki.orders.dto

import play.api.libs.json.{Format, Json}

object UserDetails {
  case class UserDetailsDto(orderId: Long, username: String, cardAuthorizationInfo: String)

  implicit val userDetailsDtoFormat: Format[UserDetailsDto] = Json.format[UserDetailsDto]
}
