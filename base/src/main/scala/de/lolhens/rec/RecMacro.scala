package de.lolhens.rec

import scala.reflect.macros.blackbox

object RecMacro {
  def applyImpl[A](c: blackbox.Context)(rec: c.Expr[Rec[A]])(stackLimit: c.Expr[StackLimit]): c.Expr[Rec[A]] = ???
}
