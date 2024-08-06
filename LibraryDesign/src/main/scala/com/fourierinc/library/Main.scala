package com.fourierinc.library

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directive._
import akka.stream.ActorMaterializer
import com.fourierinc.library.routes._

import scala.io.StdIn