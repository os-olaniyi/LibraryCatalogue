package com.fourierinc.library

import akka.http.scaladsl.server.Directive.*
import akka.http.scaladsl.server.PathMatchers.IntNumber
import akka.http.scaladsl.server.Route
import com.fourierinc.library.services.BorrowService

import scala.concurrent.ExecutionContext


object BorrowRoutes {
  def route(implicit ec: ExecutionContext): Route =
    pathPrefix("borrow") {
      (post & path(IntNumber / "user" / IntNumber)) { (bookId, userId) =>
        complete(BorrowService.borrowBook(userId, bookId))
      } ~
        (post & path("return" / IntNumber/ "user" / IntNumber)) { (bookId, userID) => 
          complete(BorrowService.returnBook(userId, bookId))
        }
    }
}