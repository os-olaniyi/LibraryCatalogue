package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables.*
import com.fourierinc.library.databases.DatabaseConfig.*
import com.fourierinc.library.models.{Book, BorrowRecord}
import slick.jdbc.MySQLProfile.api.*

import scala.concurrent.{ExecutionContext, Future}
import java.sql.Date
import java.time.{LocalDate, LocalDateTime}

object BorrowService {
  def borrowBook (userId: Int, bookId: Int)(implicit executionContext: ExecutionContext):
  Future[Either[String, BorrowRecord]] = {
    val dateBorrowed = Date.valueOf(LocalDate.now())
    val dueData = Date.valueOf(LocalDate.now().plusDays(21))  //15 working days (Mon-Fri)
    
    val action  = for {
      bookOpt <- books.filter(_.id === bookId).result.headOption
      result <- bookOpt match {
        case Some(book) if book.available =>
          val borrowRecord = BorrowRecord(0, userId, bookId, dateBorrowed, dueData, returned = false, LocalDateTime.now())
          for {
            _ <- books.filter(_.id === bookId).map(_.available).update(false)
            recordId <- borrowRecords returning borrowRecords.map(_.id) += borrowRecord
          } yield Right(borrowRecord.copy(id = recordId))
        case Some(_) => DBIO.successful(Left("Book is not available"))
        case None => DBIO.successful(Left("Book not found"))
      }
    } yield result
    db.run(action.transactionally)
  }
  
  def  returnBook(userId: Int, bookId: Int)(implicit executionContext: ExecutionContext):
  Future[Either[String, Book]] = {
    val action = for {
      recordOpt <- borrowRecords.filter(br => br.userId === userId && br.bookId === bookId && !br.returned).result.headOption
      result <- recordOpt match {
        case Some(record) => 
          for {
            _ <- borrowRecords.filter(_.id === record.id).map(_.returned).update(true)
            bookOpt <- books.filter(_.id === record.bookId).result.headOption
            - <- books.filter(_.id === record.bookId).map(_.available).update(true)
          } yield bookOpt match {
            case Some(book) => Right(book)
            case None => Left("Book not found")
          }
        case None => DBIO.successful(Left("Borrow record not found")) 
      }
    }yield result
    db.run(action.transactionally)
  }
  
  def getBorrowRecordByUserId(userId: Int)(implicit ec: ExecutionContext):
  Future[Seq[BorrowRecord]] = {
    db.run(borrowRecords.filter(_.userId === userId).result)
  }
}