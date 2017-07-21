package com.inkenkun.x1.scala.monix

// In order to evaluate tasks, we'll need a Scheduler
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global

// A Future type that is also Cancelable
import monix.execution.CancelableFuture

// Task is in monix.eval
import monix.eval.Task

import scala.concurrent.{Await, TimeoutException}
import scala.concurrent.duration._
import scala.util.{Try, Success, Failure}

object TaskExample {

  def await: String = {
    val start = System.currentTimeMillis

    val tasks = for {
      i <- 1 to 5
    } yield {
      val t = Task {
        if (i==2 || i==4)
          Thread.sleep(1000)
        println(s"Effect $i")
        1
      }
      val timedOut = t.timeout( 10 millis )
      val materialized = timedOut.materialize
      materialized.flatMap {
        case Success(value) => Task.now(Right(value))
        case Failure(t: Throwable) => Task.now(Left(t))
      }
    }

    val list: Task[Seq[Either[Throwable, Int]]] =
      Task.gatherUnordered(tasks).timeout(100 millis)

//    val completed = for (r <- list) { println(r) }
    val completed = list.runAsync
    val res = Await.result(completed, 100 millis)
    println(res)

//    list.runAsync.foreach(println)
    println(s"time: ${System.currentTimeMillis - start}")
    res.toString
  }

  def noAwait(): Unit = {
    val start = System.currentTimeMillis

    val tasks = for {
      i <- 1 to 5
    } yield {
      val t = Task {
        if (i==2 || i==4)
          Thread.sleep(1000)
        println(s"Effect $i")
        1
      }
      val timedOut = t.timeout( 10 millis )
      val materialized = timedOut.materialize
      materialized.flatMap {
        case Success(value) => Task.now(Right(value))
        case Failure(t: Throwable) => Task.now(Left(t))
      }
    }

    val list: Task[Seq[Either[Throwable, Int]]] =
      Task.gatherUnordered(tasks)

    val completed: Cancelable = list.runOnComplete {
      case Success(s) => println(s)
      case Failure(t) => println(t)
    }
    println(completed)

    //    list.runAsync.foreach(println)
    println(s"time: ${System.currentTimeMillis - start}")
  }
}
