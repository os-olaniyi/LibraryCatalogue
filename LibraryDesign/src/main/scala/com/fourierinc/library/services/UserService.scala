package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables._
import com.fourierinc.library.databases.DatabaseConfig._
import com.fourierinc.library.models.User
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

import org.mindrot.jbcrypt.BCrypt
//import com.github.t3hnar.bcrypt._

object UserService {
  def generateRegistrationId(user: User): String = {
    val dobString = user.dateOfBirth.toString.replace("-", "")
    val firstNameInitial = user.firstName.charAt(0).toUpper
    val random = Random.alphanumeric.filter(_.isLetterOrDigit).take(4).mkString
    s"$dobString-$firstNameInitial$random"
  }
  
  def hashPassword(user: User): String = {
    val password = user.passwordHash
    BCrypt.hashpw(password, BCrypt.gensalt(10))
  }
  
  def registerUser (user: User)(implicit ec: ExecutionContext): Future[Int] = {
    val registrationId = generateRegistrationId(user)
    val hashedPassword = hashPassword(user)
    //val hashedPassword = BCrypt.hashpw(user.passwordHash, BCrypt.gensalt(10))
    val userWithId = user.copy(registrationId = registrationId, passwordHash = hashedPassword)
    db.run(users returning users.map(_.id) += userWithId)
  }
}