package de.lolhens.rec

import scala.reflect.macros.{blackbox, whitebox}

object RecMacro {
  def applyImpl[A](c: blackbox.Context)(rec: c.Tree)(stackLimit: c.Expr[StackLimit]): c.Expr[Rec[A]] = {
    import c.universe._

    c.Expr(
      q"""
         if ($stackLimit.isReached)
           de.lolhens.rec.Rec.defer {
             $rec
           }
         else
           de.lolhens.rec.Rec.eval {
             $rec
           }
       """
    )
  }

  def recImpl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = {
      annottees.map(_.tree).toList match {
        case q"$mods def $methodName[..$tpes](...$args): $returnType = { ..$body }" :: Nil =>
          q"""$mods def $methodName[..$tpes](...$args)(implicit stackLimit: de.lolhens.rec.StackLimit.Next = de.lolhens.rec.StackLimit.initial): de.lolhens.rec.Rec[$returnType] = {
            val stackLimit_tmp: StackLimit.Next = stackLimit

            {
              val stackLimit: StackLimit.Next = stackLimit_tmp
              implicit val stackLimit_prev: StackLimit = stackLimit_tmp.stackLimit

              Rec {
                ..$body
              }
            }
          }"""


        case _ =>
          c.abort(c.enclosingPosition, "Annotation @recursive can be used only with methods")
      }
    }

    c.Expr[Any](result)
  }
}
