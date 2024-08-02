package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables._
import com.fourierinc.library.databases.DatabaseConfig._
import com.fourierinc.library.models.{Book, B}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global


object BookService {
  def addBook(book: Book)(implicit ec: ExecutionContext): Future[Int] = {
    db.run(books returning books.map(_.id) += book)
  }
  
  def assignShelf(bookId: Int, shelfNumber: Int)(implicit ec: ExecutionContext): Future[Int] ={
    val query = for (book <- books if book.id === bookId) yield  book.shelfNumber
    db.run(query.update(shelfNumber))
  }
  
  def listBooks(subject: Option[String])(implicit ec: ExecutionContext):
  Future[Seq[Book]] = {
    subject match {
      case Some(sub) => db.run(books.filter(_.subject === sub).result)
      case None => db.run(books.result)
    }
  }
}