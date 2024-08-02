package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables._
import com.fourierinc.library.databases.DatabaseConfig._
import com.fourierinc.library.models.Session

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import java.time.LocalDateTime
import java.util.UUID


object SessionService {
  def createSession(userId: Int)(implicit ec: ExecutionContext): Future[Int] = {
    val sessionId = UUID.randomUUID()
    val loginTime = LocalDateTime.now()
    val session = Session(0, userId, sessionId, loginTime, None, active = true, LocalDateTime.now())
    db.run(sessions returning sessions.map(_.id) += session)
  }
  def endSession (sessionId: UUID)(implicit ec: ExecutionContext): Future[Int] = {
    val query = for {
      session <- sessions if session.sessionId === sessionId && session.active
    } yield (session.logoutTime, session.active)
    db.run(query.update(Some(LocalDateTime.now()), false))
  }
  
  def getSessionByUserId(userId: Int)(implicit ec: ExecutionContext): Future[Option[Session]] = {
    db.run(sessions.filter(s => s.userId === userId && s.active).result.headOption)
  }
}