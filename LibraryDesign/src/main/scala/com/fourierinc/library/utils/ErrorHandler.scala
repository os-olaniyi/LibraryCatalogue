package com.fourierinc.library.utils

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._ 
import akka.http.scaladsl.server.ExceptionHandler

object ErrorHandler {
  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ex: Exception =>
        extractUri { uri =>
          complete((StatusCodes.InternalServerError,
            s"Internal server error: ${ex.getMessage}"))
        }
    }
}
