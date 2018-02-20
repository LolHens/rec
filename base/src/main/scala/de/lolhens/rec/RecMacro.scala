package de.lolhens.rec

import scala.reflect.macros.{blackbox, whitebox}

object RecMacro {
  def applyImpl[A](c: blackbox.Context)(rec: c.Tree)(stackDepth: c.Expr[StackDepth]): c.Expr[Rec[A]] = {
    import c.universe._

    c.Expr(
      q"""
         if ($stackDepth.isReached)
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
          q"""$mods def $methodName[..$tpes](...$args)(implicit nextStackDepth: de.lolhens.rec.StackDepth.Next = de.lolhens.rec.StackDepth.initial): de.lolhens.rec.Rec[$returnType] = {
            val nextStackDepth_tmp: de.lolhens.rec.StackDepth.Next = nextStackDepth

            {
              val nextStackDepth: de.lolhens.rec.StackDepth.Next = nextStackDepth_tmp
              val stackDepth: de.lolhens.rec.StackDepth = nextStackDepth_tmp.stackDepth

              val result = if (stackDepth.isReached) {
                implicit val stackDepth_empty: de.lolhens.rec.StackDepth.Next = stackDepth.reset
                de.lolhens.rec.Rec.defer {
                  ..$body
                }
              } else {
                implicit val stackDepth_empty: de.lolhens.rec.StackDepth.Next = stackDepth.next
                de.lolhens.rec.Rec.eval {
                  ..$body
                }
              }

              result
            }
          }"""


        case _ =>
          c.abort(c.enclosingPosition, "Annotation @recursive can be used only with methods")
      }
    }

    c.Expr[Any](result)
  }
}
