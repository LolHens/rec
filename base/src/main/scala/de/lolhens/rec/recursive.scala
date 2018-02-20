package de.lolhens.rec

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros

class recursive extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro RecMacro.recImpl
}
