package de.lolhens.rec

case class StackDepth(depth: Int) extends AnyVal {
  def isReached: Boolean =
    depth >= StackDepth.maxStackDepth

  def isInitial: Boolean =
    depth == -1

  def next: StackDepth.Next =
    new StackDepth.Next(depth + 1)

  def reset: StackDepth.Next =
    new StackDepth.Next(0)
}

object StackDepth {
  val initial: StackDepth.Next = new StackDepth.Next(-1)

  @volatile
  var maxStackDepth = 1000

  class Next(val limit: Int) extends AnyVal {
    def stackDepth: StackDepth =
      new StackDepth(limit)
  }

  implicit def nextStackLimit(implicit stackLimit: StackDepth): StackDepth.Next = if (stackLimit.isReached) stackLimit.reset else stackLimit.next
}
