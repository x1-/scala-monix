package com.inkenkun.x1.scala.monix

import java.util.concurrent.{ExecutorService, Executors}

import org.http4s.dsl._
import org.http4s._
import org.http4s.headers.`User-Agent`
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.{Server, ServerApp}

import scalaz.concurrent.Task

object Main extends ServerApp {

  val ok = Ok("ok").withContentType(Some(MediaType.`text/plain`))
  val service = HttpService {
    case req @ GET -> Root / "gather"  =>
      val s = TaskExample.await
      Ok(s).withContentType(Some(MediaType.`text/plain`))
    case req @ GET -> Root / "noawait"  =>
      TaskExample.noAwait()
      ok
  }

  override def server(args: List[String]): Task[Server] = {
    val pool : ExecutorService = Executors.newCachedThreadPool()
    BlazeBuilder
      .bindHttp(8080, "0.0.0.0")
      .mountService(service, "/")
      .withServiceExecutor(pool)
      .start
  }
}
