package mikraservisiki.payment.routing

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{FutureDirectives, MethodDirectives, PathDirectives, RouteDirectives}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import mikraservisiki.payment.dto.UserDetails.UserDetailsDto
import mikraservisiki.payment.handler.PaymentService

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
      (path(LongNumber) & put & entity(as[UserDetailsDto])) { (orderId, UserDetailsDto) =>
        onSuccess(paymentService.performPayment(orderId, UserDetailsDto.username, UserDetailsDto.cardAuthorizationInfo)) {
          case Some(order) => complete(OrderDto(order.id))
          case None => complete(StatusCodes.Unauthorized)
        }
      }
    }
}
