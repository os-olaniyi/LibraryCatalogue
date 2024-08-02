package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables._
import com.fourierinc.library.databases.DatabaseConfig._
import com.fourierinc.library.databases.{Book => DbBook}
import com.fourierinc.library.models.{Book => ModelBook}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global


object BookService {
  def addBook(book: ModelBook)(implicit ec: ExecutionContext): Future[Int] = {
    val dbBook = DbBook(book.id, book.title, book.author, book.isbn, book.subject, book.publishedYear, book.shelfNumber, book.bookTag, book.available, book.createdAt)
    db.run(books returning books.map(_.id) += dbBook)
  }
  
  def assignShelf(bookId: Int, shelfNumber: Int)(implicit ec: ExecutionContext): Future[Int] ={
    val query = for (book <- books if book.id === bookId) yield  book.shelfNumber
    db.run(query.update(shelfNumber))
  }
  
  def listBooks(subject: Option[String])(implicit ec: ExecutionContext):
  Future[Seq[ModelBook]] = {
    val action =subject match {
      case Some(sub) => books.filter(_.subject === sub).result
      case None => books.result
    }
    db.run(action).map(_.map(dbBook => ModelBook(dbBook.id, dbBook.title,
      dbBook.author, dbBook.isbn, dbBook.subject, dbBook.publishedYear, dbBook.shelfNumber, dbBook.bookTag, dbBook.available, dbBook.createdAt
    )))
  }
}