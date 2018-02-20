package de.lolhens.rec

import scala.annotation.tailrec
import scala.language.experimental.macros

class Rec[A](private val comp: Any) extends AnyVal {
  @tailrec
  final def value: A = comp match {
    case deferred: Deferred[A] => deferred.value.value
    case e: A => e
  }
}

object Rec {
  def now[A](value: A): Rec[A] = new Rec(value)

  def eval[A](comp: Rec[A]): Rec[A] = comp

  def defer[A](f: => Rec[A]): Rec[A] = new Rec(new Deferred(f))

  def apply[A](rec: => Rec[A])(implicit stackDepth: StackDepth): Rec[A] = macro RecMacro.applyImpl[A]

  implicit def implicitNow[A](value: A): Rec[A] = now(value)

  implicit def recValue[A](rec: Rec[A]): A = rec.value
}
