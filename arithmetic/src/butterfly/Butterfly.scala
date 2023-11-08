package arithmetic.butterfly

import chisel3._
import chisel3.util.{isPow2, log2Up}

/** A trait representing a butterfly network.
  * Butterfly Network: https://en.wikipedia.org/wiki/Butterfly_network
  */
trait Butterfly[T <: Data] {
  def op(in: (T, T)): (T, T)

  def butterfly(in: Seq[T]): Seq[T] = {
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
