package mikraservisiki.payment.routing

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{FutureDirectives, MethodDirectives, PathDirectives, RouteDirectives}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import mikraservisiki.payment.dto.PaymentInfo.PaymentInfoDto
import mikraservisiki.payment.handler.PaymentService
import mikraservisiki.payment.handler.payments.AuthorizationFailedException

import scala.util.{Failure, Success}

object AppRouting extends RouteDirectives
  with PathDirectives
  with MethodDirectives
  with PlayJsonSupport
  with FutureDirectives {

  import mikraservisiki.payment.dto.Orders._

  def route(
             paymentService: PaymentService
           ): Route =
    pathPrefix("payment") {
      (path(LongNumber) & put & entity(as[PaymentInfoDto])) { (orderId, paymentInfoDto) =>
        onComplete(paymentService.performPayment(orderId, paymentInfoDto.amount, paymentInfoDto.cardAuthorizationInfo)) {
          case Success(orderDto) => complete(orderDto)
          case Failure(AuthorizationFailedException) => complete(StatusCodes.Unauthorized)
          case Failure(e: IllegalArgumentException) => complete(StatusCodes.BadRequest, e.getMessage)
          case Failure(e: RuntimeException) => complete(StatusCodes.InternalServerError, e.getMessage)
          case _ => complete(StatusCodes.InternalServerError)
        }
      }
    }
}
