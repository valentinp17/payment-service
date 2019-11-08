package mikraservisiki.payment.dto

import play.api.libs.json.{Format, Json}

object PaymentInfo {

  case class PaymentInfoDto(amount: BigDecimal, cardAuthorizationInfo: String)

  implicit val paymentInfoDtoFormat: Format[PaymentInfoDto] = Json.format[PaymentInfoDto]
}
