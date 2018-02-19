package de.lolhens.rec

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]): Unit = {
    def even(i: Long)(implicit stackLimit: StackLimit = StackLimit.initial): Rec[Boolean] = i match {
      case 0 => Rec.now(true)
      case _ =>
        Rec(odd(i - 1)(stackLimit.next))
    }

    def odd(i: Long)(implicit stackLimit: StackLimit = StackLimit.initial): Rec[Boolean] = i match {
      case 0 => Rec.now(false)
      case _ =>
        Rec(even(i - 1)(stackLimit.next))
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

    println(even(500000000L).value)
    println(test2.run(test2.even(500000000L)))

    val time1 = System.currentTimeMillis()

    println(even(5000000000L).value)

    val time2 = System.currentTimeMillis()
    println(time2 - time1)

    println(test2.run(test2.even(5000000000L)))

    val time3 = System.currentTimeMillis()
    println(time3 - time2)

    //println("asdf")
  }
}
