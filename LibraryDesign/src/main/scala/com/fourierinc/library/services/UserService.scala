package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables.*
import com.fourierinc.library.databases.DatabaseConfig.*
import com.fourierinc.library.models.{User => ModelUser}
import com.fourierinc.library.databases.{User => DbUser}
import slick.jdbc.MySQLProfile.api.*

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
import org.mindrot.jbcrypt.BCrypt
import sun.security.util.Password
//import com.github.t3hnar.bcrypt._

object UserService {
  def generateRegistrationId(user: ModelUser): String = {
    val dobString = user.dateOfBirth.toString.replace("-", "")
    val firstNameInitial = user.firstName.charAt(0).toUpper
    val random = Random.alphanumeric.filter(_.isLetterOrDigit).take(4).mkString
    s"$dobString-$firstNameInitial$random"
  }
  
  def hashPassword(user: ModelUser): String = {
    val password = user.passwordHash
    BCrypt.hashpw(password, BCrypt.gensalt(10))
  }
  
  def registerUser (user: ModelUser)(implicit ec: ExecutionContext): Future[Int] = {
    val registrationId = generateRegistrationId(user)
    val hashedPassword = hashPassword(user)     //Works for jbcrypt 
    //val hashedPassword = user.passwordHash.bcrypt
    //val hashedPassword = BCrypt.hashpw(user.passwordHash, BCrypt.gensalt(10))   //Works for jbcrypt 
    val userWithId = user.copy(registrationId = registrationId, passwordHash = hashedPassword)
    
    
    val dbUser = DbUser(
      userWithId.id, userWithId.firstName, userWithId.lastName, userWithId.email, userWithId.dateOfBirth,
      userWithId.registrationId, userWithId.passwordHash, userWithId.passportPicture, userWithId.createdAt
    )
    db.run(users returning users.map(_.id) += dbUser)
  }
  
  def authenticate(email: String, password: String)(implicit ec: ExecutionContext): Future[Option[ModelUser]] = {
    db.run(users.filter(_.email === email).result.headOption).map {
      case Some(dbUser) if BCrypt.checkpw(password, dbUser.passwordHarsh) => 
        Some(ModelUser(
          dbUser.id, dbUser.firstName, dbUser.lastName, dbUser.email, dbUser.dateOfBirth, dbUser.registrationId, dbUser.passwordHarsh, dbUser.passportPicture, dbUser.createdAt
        ))
      case _  => None  
    }
  }
}