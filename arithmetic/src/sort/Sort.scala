package arithmetic.sort

import chisel3._
import chisel3.util.{isPow2, log2Up}
import arithmetic.butterfly.Butterfly

/**
  * Bitonic Sorter
  *
  * @see [[https://en.wikipedia.org/wiki/Bitonic_sorter]]
  *
  * @tparam T The type of data to be sorted.
  * @param in The input sequence to be sorted.
  * @param casOp The Compare-And-Swap operation.
  * @return The sorted sequence.
  */
object BitonicSort {
  def apply[T <: Data](in: Seq[T])(casOp: ((T, T)) => (T, T)): Seq[T] = {
    require(isPow2(in.length))
    (1 to log2Up(in.length)).foldLeft(in) { case (prev, index) =>
      prev
        .grouped(1 << index)
        .zipWithIndex
        .map { case (group, i) =>
          if ((i & 1) == 0) Butterfly(group)(casOp) else Butterfly(group)(casOp).reverse
        }
        .flatten
        .toSeq
    }
  }
}
