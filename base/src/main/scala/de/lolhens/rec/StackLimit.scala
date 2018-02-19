package de.lolhens.rec

case class StackLimit(limit: Int) extends AnyVal {
  def isReached: Boolean = limit <= 0

  def next: StackLimit =
    if (isReached)
      StackLimit.initial
    else
      StackLimit(limit - 1)
}

object StackLimit {
  @volatile
  var initial = StackLimit(1000)
}
