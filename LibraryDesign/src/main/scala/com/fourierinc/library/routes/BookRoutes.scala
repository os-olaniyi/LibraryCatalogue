package com.fourierinc.library.routes

import akka.http.scaladsl.server.Directive._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import akka.http.scaladsl.server.Route
import com.fourierinc.library.services.BookService
import com.fourierinc.library.models.{Book => ModelBook}

import scala.concurrent.ExecutionContext


object BookRoutes {
  def route(implicit ec: ExecutionContext): Route =
    pathPrefix("books") {
      (post & entity(as[ModelBook])) { book =>
        complete(BookService.addBook(book))
      } ~
        (put & path(IntNumber / "shelf" / IntNumber)) { (bookId, shelfNumber) =>
          complete(BookService.assignShelf(bookId, shelfNumber))
        } ~
        get {
          parameter("subject".?) { subject =>
            complete(BookService.listBooks(subject))
          }
        }
    }
}