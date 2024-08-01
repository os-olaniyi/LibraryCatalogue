package com.fourierinc.library.models

import java.time.LocalDateTime

case class Book (
                id: Int,
                title: String,
                author: String,
                isbn: String,
                subject: String,
                publishedYear: Int,
                shelfNumber: Int,
                bookTag: String,
                available: Boolean,
                createdAt: LocalDateTime
                )