package de.lolhens.rec

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object RecMacro {
  def applyImpl[A](c: blackbox.Context)(rec: c.Expr[Rec[A]])(stackLimit: c.Expr[StackLimit]): c.Expr[Rec[A]] = {
    import c.universe._

    c.Expr(
      q"""
          if ($stackLimit.isReached) {
            de.lolhens.rec.Rec.defer($rec)
          } else {
            de.lolhens.rec.Rec.eval($rec)
          }
       """
    )
  }
}
