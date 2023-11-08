package arithmetic.butterfly

import chisel3._
import chisel3.util.{isPow2, log2Up}

/**
  * Butterfly Network.
  *
  * @see [[https://en.wikipedia.org/wiki/Butterfly_network]]
  */
object Butterfly {
  def apply[T <: Data](in: Seq[T])(op: ((T, T)) => (T, T)): Seq[T] = {
    require(isPow2(in.length))
    (1 to log2Up(in.length)).foldLeft(in) { case (prev, rank) =>
      prev
        .grouped(prev.length >> (rank - 1))
        .map { group =>
          val (lo, hi) = group.splitAt(group.length / 2) match {
            case (l, r) => (l zip r).map(op).unzip
          }
          lo ++ hi
        }
        .flatten
        .toSeq
    }
  }
}
