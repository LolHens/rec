package de.lolhens.rec

protected class Deferred[A](f: => Rec[A]) {
  def value: Rec[A] = f
}
