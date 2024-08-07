package com.fourierinc.library.routes

import akka.http.scaladsl.server.Directive._
import akka.http.scaladsl.server.Route
import com.fourierinc.library.services.UserService
import com.fourierinc.library.models.{User => ModelUser}
import scala.concurrent.ExecutionContext

object UserRoutes {
  def route(implicit ec: ExecutionContext): Route =
    pathPrefix("users") {
      (post & entity(as[ModelUser])) {user =>
        complete(UserService.registerUser(user))
      } ~
        (post & path("authenticate") & entity(as[LoginRequest]))
      { loginRequest =>
        complete(UserService.authenticate(loginRequest.email,
          loginRequest.password))
      }
    }
}