package com.fourierinc.library.services

import akka.http.scaladsl.server.Directive._
import akka.http.scaladsl.server.{Directive1, Route}
import scala.concurrent.{ExecutionContext, Future}
import akka.http.scaladsl.server.directives.Credentials

object AuthorizationService {
  def authorize(credentials: Credentials)(implicit  ec:
  ExecutionContext): Future[Boolean] = {
    credentials match {
      case p@Credentials.Provided(token) =>
        Future.successful(true)
      case _ => Future.successful(false)
    }
  }
  def authenticated(implicit ec: ExecutionContext):
  Directive1[String] =
    authenticateOAuth2Async("secure-site", AuthorizationService.authorize)
}