package com.fourierinc.library.databases

import slick.jdbc.MySQLProfile.api._
import  com.typesafe.config.ConfigFactory
import  scala.concurrent.ExecutionContext.Implicits.global

object DatabaseConfig {
  val config = ConfigFactory.load()
  val db = Database.forConfig("libraryDb")
}
  
