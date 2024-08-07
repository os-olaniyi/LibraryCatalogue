package com.fourierinc.library

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directive._
import akka.stream.ActorMaterializer
import com.fourierinc.library.routes._
import com.fourierinc.library.utils.ErrorHandler

import scala.io.StdIn

object Main extends App with ErrorHandler {
  implicit val system = ActorSystem("library-system")
  implicit val materializer = ActorMaterializer
  implicit val executionContext = system.dispatcher
  
  val routes =
    handleExceptions(ErrorHandler.exceptionHandler){
      BookRoutes.route ~
      UserRoutes.route ~
      SessionRoutes.route ~
      BorrowRoutes.route
    }
    
  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)
  
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop.....")
  StdIn.readLine()
  bindingFuture.flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}