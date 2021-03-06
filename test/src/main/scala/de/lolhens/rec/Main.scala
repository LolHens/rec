package de.lolhens.rec

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]): Unit = {
    def even(i: Long)(implicit stackLimit: StackLimit.Next = StackLimit.initial): Rec[Boolean] = {
      val stackLimit$tmp: StackLimit.Next = stackLimit

      {
        val stackLimit: StackLimit.Next = stackLimit$tmp
        implicit val stackLimit$prev: StackLimit = stackLimit$tmp.stackLimit

        Rec {
          i match {
            case 0 => Rec.now(true)
            case _ =>
              odd(i - 1)
          }
        }(stackLimit$prev)
      }
    }

    def odd(i: Long)(implicit stackLimit: StackLimit.Next = StackLimit.initial): Rec[Boolean] = {
      val stackLimit$tmp: StackLimit.Next = stackLimit

      {
        val stackLimit: StackLimit.Next = stackLimit$tmp
        implicit val stackLimit$prev: StackLimit = stackLimit$tmp.stackLimit

        Rec {
          i match {
            case 0 => Rec.now(false)
            case _ =>
              even(i - 1)
          }
        }(stackLimit$prev)
      }
    }

    object test2 {

      sealed trait EvenOdd

      case class Done(result: Boolean) extends EvenOdd

      case class Even(value: Long) extends EvenOdd

      case class Odd(value: Long) extends EvenOdd

      def even(i: Long): EvenOdd = i match {
        case 0 => Done(true)
        case _ => Odd(i - 1)
      }

      def odd(i: Long): EvenOdd = i match {
        case 0 => Done(false)
        case _ => Even(i - 1)
      }

      @tailrec
      def run(evenOdd: EvenOdd): Boolean = evenOdd match {
        case Done(result) => result
        case Even(value) => run(even(value))
        case Odd(value) => run(odd(value))
      }
    }

    object test3 {
      @recursive
      def even(i: Long): Boolean = i match {
        case 0 => true
        case _ =>
          odd(i - 1)
      }

      @recursive
      def odd(i: Long): Boolean = i match {
        case 0 => false
        case _ =>
          even(i - 1)
      }
    }

    println(test3.even(5000): Boolean)
    println(test3.odd(5000): Boolean)
    println(test3.even(5001): Boolean)
    println(test3.odd(5001): Boolean)

    println("---")

    if (true) {
      for (i <- 0 until 1)
        test3.even(5000*1000): Boolean

      println(even(500000000L).value)
      println(test2.run(test2.even(500000000L)))
    }

    if (false) {
      val time0 = System.currentTimeMillis()

      for (i <- 0 until 100000)
        test3.even(50000)

      val time1 = System.currentTimeMillis()
      println(time1 - time0)

      println(even(5000000000L).value)

      val time2 = System.currentTimeMillis()
      println(time2 - time1)

      println(test2.run(test2.even(5000000000L)))

      val time3 = System.currentTimeMillis()
      println(time3 - time2)
    }


    for (i <- 0 until 10) {
      println("---")

      val time0 = System.currentTimeMillis()

      for (i <- 0 until 100000)
        test3.even(5000L): Boolean

      val time1 = System.currentTimeMillis()
      println(time1 - time0)

      for (i <- 0 until 10000)
        even(50000L).value

      val time2 = System.currentTimeMillis()
      println(time2 - time1)

      for (i <- 0 until 10000)
        test2.run(test2.even(50000L))

      val time3 = System.currentTimeMillis()
      println(time3 - time2)
    }
    //println("asdf")
  }
}
