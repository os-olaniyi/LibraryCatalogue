package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables.*
import com.fourierinc.library.databases.DatabaseConfig.*
import com.fourierinc.library.models.{Book => ModelBook, BorrowRecord => ModelBorrowRecord}
import com.fourierinc.library.databases.{BorrowRecord => DbBorrowRecord}
import slick.jdbc.MySQLProfile.api.*

import scala.concurrent.{ExecutionContext, Future}
import java.sql.Date
import java.time.{LocalDate, LocalDateTime}

object BorrowService {
  def borrowBook (userId: Int, bookId: Int)(implicit executionContext: ExecutionContext):
  Future[Either[String, ModelBorrowRecord]] = {
    val dateBorrowed = Date.valueOf(LocalDate.now())
    val dueData = Date.valueOf(LocalDate.now().plusDays(21))  //15 working days (Mon-Fri)
    
    val action  = for {
      bookOpt <- books.filter(_.id === bookId).result.headOption
      result <- bookOpt match {
        case Some(book) if book.available =>
          val borrowRecord = ModelBorrowRecord(0, userId, bookId, dateBorrowed, dueData, returned = false, LocalDateTime.now())
          val dbBorrowRecord = DbBorrowRecord(
            borrowRecord.id, borrowRecord.userId, borrowRecord.bookId, borrowRecord.dateBorrowed,
            borrowRecord.dueDate, borrowRecord.returned, borrowRecord.createdAt
          )
          for {
            _ <- books.filter(_.id === bookId).map(_.available).update(false)
            recordId <- borrowRecords returning borrowRecords.map(_.id) += dbBorrowRecord
          } yield Right(borrowRecord.copy(id = recordId))
        case Some(_) => DBIO.successful(Left("Book is not available"))
        case None => DBIO.successful(Left("Book not found"))
      }
    } yield result
    db.run(action.transactionally)
  }
  
  def  returnBook(userId: Int, bookId: Int)(implicit executionContext: ExecutionContext):
  Future[Either[String, ModelBook]] = {
    val action = for {
      recordOpt <- borrowRecords.filter(br => br.userId === userId && br.bookId === bookId && !br.returned).result.headOption
      result <- recordOpt match {
        case Some(record) => 
          for {
            _ <- borrowRecords.filter(_.id === record.id).map(_.returned).update(true)
            bookOpt <- books.filter(_.id === record.bookId).result.headOption
            - <- books.filter(_.id === record.bookId).map(_.available).update(true)
          } yield bookOpt match {
            case Some(book) => Right(ModelBook(
              book.id, book.title, book.author, book.isbn, book.subject, book.publishedYear, book.shelfNumber, book.bookTag, book.available, book.createdAt
            ))
            case None => Left("Book not found")
          }
        case None => DBIO.successful(Left("Borrow record not found")) 
      }
    }yield result
    db.run(action.transactionally)
  }
  
  def getBorrowRecordByUserId(userId: Int)(implicit ec: ExecutionContext):
  Future[Seq[ModelBorrowRecord]] = {
    db.run(borrowRecords.filter(_.userId === userId).result).map { dbRecords => dbRecords.map {
      dbRecord => 
        ModelBorrowRecord(
          dbRecord.id, dbRecord.userId, dbRecord.bookId, dbRecord.dateBorrowed, dbRecord.dueDate, dbRecord.returned, dbRecord.createdAt
        )
    }}
  }
}