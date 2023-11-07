package arithmetic.butterfly

import chisel3._
import chisel3.util._

// Butterfly Network: https://en.wikipedia.org/wiki/Butterfly_network
trait Butterfly[T <: Data] {
  def op(in: (T, T)): (T, T)

  private def layer(rank: Int, in: Seq[T]): Seq[T] = {
    require(rank <= log2Up(in.length))
    require(isPow2(in.length))

    val next = in
      .grouped(in.length >> (rank - 1))
      .map { s =>
        val ss       = s.splitAt(s.length / 2)
        val (lo, hi) = (ss._1 zip ss._2).map(op(_)).unzip
        lo ++ hi
      }
      .flatten
      .toSeq

    if (rank < log2Up(in.length))
      layer(rank + 1, next)
    else
      next
  }

  def butterfly(in: Seq[T]): Seq[T] = {
    require(isPow2(in.length))
    layer(1, in)
  }
}
