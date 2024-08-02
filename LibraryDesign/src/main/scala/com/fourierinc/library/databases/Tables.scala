package com.fourierinc.library.databases

import slick.jdbc.MySQLProfile.api.*
import slick.lifted.{ProvenShape, ForeignKeyQuery}
import scala.concurrent.ExecutionContext.Implicits.global

import java.sql.Date
import java.time.LocalDateTime

case class User(id: Int, firstName: String, lastName: String, dateOfBirth: Date, registrationId: String, passwordHarsh: String,
                passportPicture: Array[Byte], createdAt: LocalDateTime)
case class Book(id: Int, title: String, author: String, isbn: String, subject: String, publishedYear: Int, shelfNumber: Int,
                bookTag: String, available: Boolean, createdAt: LocalDateTime)
case class BorrowRecord(id: Int, userId: Int, bookId: Int, dateBorrowed: Date, dueDate: Date,
                        returned: Boolean, createdAt: LocalDateTime)
case class Session(id: Int, userId: Int, sessionId: java.util.UUID, loginTime: LocalDateTime, logoutTime: Option[LocalDateTime],
                   active: Boolean, createdAt: LocalDateTime)

class Users(tag: Tag)
  extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def dateOfBirth = column[Date]("date_of_birth")
  def registrationId = column[String]("registration_id")
  def passwordHash = column[String]("password_hash")
  def passportPicture = column[Array[Byte]]("passport_picture")
  def createdAt = column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))
  def * = (id, firstName, lastName, dateOfBirth, registrationId, passwordHash, passportPicture, createdAt).mapTo[User]

//  //def * : ProvenShape[User] = ???
//  def * : ProvenShape[User] = (id, firstName, lastName, dateOfBirth, registrationId, passwordHash,
//    passportPicture, createdAt) <> (User.tupled, User.unapply) 
}

class Books(tag: Tag)
  extends Table[Book](tag, "books"){
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def author = column[String]("author")
  def isbn = column[String]("isbn")
  def subject = column[String]("subject")
  def publishedYear = column[Int]("published_year")
  def shelfNumber = column[Int]("shelf_number")
  def bookTag = column[String]("book_tag")
  def available = column[Boolean]("available", O.Default(true))
  def createdAt = column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))
  def * = (id, title, author, isbn, subject, publishedYear, shelfNumber, bookTag, available, createdAt).mapTo[Book]
  
//  def * : ProvenShape[Book] = (id, title, author, isbn, subject, publishedYear, shelfNumber, bookTag,
//    available, createdAt) <> (Book.tupled, Book.unapply)
}

class BorrowRecords(tag: Tag) 
  extends Table[BorrowRecord](tag, "borrow_records") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("user_id")
  def bookId = column[Int]("book_id")
  def dateBorrowed = column[Date]("date_borrowed")
  def dueDate = column[Date]("due_date")
  def returned = column[Boolean]("returned", O.Default(false))
  def createdAt = column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))
  def * = (id, userId, bookId, dateBorrowed, dueDate, returned, createdAt).mapTo[BorrowRecord]
  
//  def * : ProvenShape[BorrowRecord] = (id, userId, bookId, dateBorrowed, dueDate, returned, createdAt) <> 
//    (BorrowRecord.tupled, BorrowRecord.unapply)
  def userFk: ForeignKeyQuery[Users, User] = foreignKey("user_fk", userId, TableQuery[Users])(_.id)
  def bookFk: ForeignKeyQuery[Books, Book] = foreignKey("book_fk", bookId, TableQuery[Books])(_.id)
}

class Sessions(tag: Tag) 
  extends Table[Session](tag, "sessions") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("user_id")
  def sessionId = column[java.util.UUID]("session_id")
  def loginTime = column[LocalDateTime]("login_time")
  def logoutTime = column[Option[LocalDateTime]]("logout_time")
  def active = column[Boolean]("active", O.Default(true))
  def createdAt = column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))
  
  def * : ProvenShape[Session] = (id, userId, sessionId, loginTime, logoutTime, active, createdAt) <>
    (Session.tupled, Session.unapply)
}

object Tables {
  val users = TableQuery[Users]
  val books = TableQuery[Books]
  val borrowRecords = TableQuery[BorrowRecords]
  val sessions = TableQuery[Sessions]
}