package com.fourierinc.library.services

import com.fourierinc.library.databases.Tables._
import com.fourierinc.library.databases.DatabaseConfig._
import com.fourierinc.library.models.{Session => ModelSession}
import com.fourierinc.library.databases.{Session => DbSession}

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import java.time.LocalDateTime
import java.util.UUID


object SessionService {
  def createSession(userId: Int)(implicit ec: ExecutionContext): Future[Int] = {
    val sessionId = UUID.randomUUID()
    val loginTime = LocalDateTime.now()
    val modelSession = ModelSession(0, userId, sessionId, loginTime, None, active = true, LocalDateTime.now())
    
    val dbSession = DbSession(
      modelSession.id, modelSession.userId, modelSession.sessionId, modelSession.loginTime,
      modelSession.logoutTime, modelSession.active, modelSession.createdAt
    )
    db.run(sessions returning sessions.map(_.id) += dbSession)
  }
  def endSession (sessionId: UUID)(implicit ec: ExecutionContext): Future[Int] = {
    val query = for {
      session <- sessions if session.sessionId === sessionId && session.active
    } yield (session.logoutTime, session.active)
    db.run(query.update(Some(LocalDateTime.now()), false))
  }
  
  def getSessionByUserId(userId: Int)(implicit ec: ExecutionContext): Future[Option[ModelSession]] = {
    db.run(sessions.filter(s => s.userId === userId && s.active).result.headOption).map {
      case Some(dbSession) =>
        Some(ModelSession(
          dbSession.id, dbSession.userId, dbSession.sessionId, dbSession.loginTime, dbSession.logoutTime, dbSession.active, dbSession.createdAt
        ))
      case None => None  
    }
  }
}