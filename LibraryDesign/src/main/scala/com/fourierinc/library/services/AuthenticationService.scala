package com.fourierinc.library.services

import com.fourierinc.library.models.{User => ModelUser}
//import com.fourierinc.library.services.UserService
import scala.concurrent.{ExecutionContext, Future}
import org.mindrot.jbcrypt.BCrypt

object AuthenticationService {
  def authenticate(email: String, password: String)(implicit ec: ExecutionContext):
  Future[Option[ModelUser]] = {
    UserService.authenticate(email, password)
  }
}