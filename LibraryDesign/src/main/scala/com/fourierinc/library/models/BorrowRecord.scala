package com.fourierinc.library.models

import java.sql.Date
import java.time.LocalDateTime

case class BorrowRecord(
                           id: Int,
                           userId: Int,
                           bookId: Int,
                           dateBorrowed: Date,
                           dueDate: Date,
                           returned: Boolean,
                           createdAt: LocalDateTime
                           )