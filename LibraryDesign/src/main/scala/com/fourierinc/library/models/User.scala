package com.fourierinc.library.models

import java.sql.Date
import java.time.LocalDateTime

case class User (
                id: Int,
                firstName: String,
                lastName: String,
                dateOfBirth: Date,
                registrationId: String,
                passwordHash: String,
                passportPicture: Array[Byte],
                createdAt: LocalDateTime
                )