package mikraservisiki.payment.dto

import play.api.libs.json.{Format, Json}

object UserDetails {
  case class UserDetailsDto(username: String, cardAuthorizationInfo: String)

  implicit val userDetailsDtoFormat: Format[UserDetailsDto] = Json.format[UserDetailsDto]
}
