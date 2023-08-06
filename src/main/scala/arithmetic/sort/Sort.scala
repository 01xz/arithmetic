package arithmetic.sort

import chisel3._
import chisel3.util._
import arithmetic.butterfly.Butterfly

trait Sort[T <: Data] {
  def casOp(a: T, b: T): (T, T)
  def sort(in: Seq[T]): Seq[T]
}

// Bitonic Sorter: https://en.wikipedia.org/wiki/Bitonic_sorter
trait Bitonic[T <: Data] extends Sort[T] with Butterfly[T] {
  def op(a: T, b: T) = casOp(a, b)

  private def block(index: Int, in: Seq[T]): Seq[T] = {
    require(index <= log2Up(in.length))
    require(isPow2(in.length))

    val next = in
      .grouped(1 << index)
      .zipWithIndex
      .map { case (s, i) =>
        if ((i & 1) == 0) butterfly(s) else butterfly(s).reverse
      }
      .flatten
      .toSeq

    if (index < log2Up(in.length))
      block(index + 1, next)
    else
      next
  }

  def sort(in: Seq[T]): Seq[T] = {
    block(1, in)
  }
}
