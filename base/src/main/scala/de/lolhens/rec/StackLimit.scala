package de.lolhens.rec

case class StackLimit(limit: Int) extends AnyVal {
  def isReached: Boolean = limit <= 0

  def next: StackLimit.Next =
    if (isReached)
      StackLimit.initial
    else
      StackLimit.Next(limit - 1)
}

object StackLimit {
  @volatile
  var initial = StackLimit.Next(1000)

  case class Next(limit: Int) extends AnyVal {
    def stackLimit = StackLimit(limit)
  }

  implicit def nextStackLimit(implicit stackLimit: StackLimit): StackLimit.Next = stackLimit.next
}
