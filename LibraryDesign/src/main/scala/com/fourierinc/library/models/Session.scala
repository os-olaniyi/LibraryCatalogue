package com.fourierinc.library.models

import java.sql.Date
import java.time.LocalDateTime
import java.util.UUID


case class Session(
                  id: Int,
                  userId: Int,
                  sessionId: UUID,
                  loginTime: LocalDateTime,
                  logoutTime: Option[LocalDateTime],
                  active: Boolean,
                  createdAt: LocalDateTime
                  )