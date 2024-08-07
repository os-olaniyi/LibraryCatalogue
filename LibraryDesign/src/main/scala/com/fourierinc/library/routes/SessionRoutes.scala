package com.fourierinc.library.routes

import akka.http.scaladsl.server.Directive.*
import akka.http.scaladsl.server.PathMatchers.IntNumber
import akka.http.scaladsl.server.Route
import com.fourierinc.library.services.SessionService

import scala.concurrent.ExecutionContext
import java.util.UUID

object SessionRoutes {
  def route(implicit ec: ExecutionContext): Route =
    pathPrefix("sessions") {
      (post & path(IntNumber)) { userId =>
        complete(SessionService.createSession(userId))
      } ~
        (post & path(JavaUUID)) { sessionId: UUID =>
          complete(SessionService.endSession(sessionId))
        }
    }
}