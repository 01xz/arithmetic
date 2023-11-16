package arithmetic.compressor

import chisel3._
import chisel3.util._
import scala.collection.mutable.Buffer

trait CompressorTree {
  def getTwoOperandsOf(bm: Seq[Seq[Bool]]): Vec[UInt]
}

trait WallaceTree extends CompressorTree with Compressor {
  def c32(in: Seq[Bool]): Seq[Bool]
  def c22(in: Seq[Bool]): Seq[Bool]

  def compress(cur: Seq[Seq[Bool]]): Seq[Seq[Bool]] = {
    val rows = cur.map(_.length).max
    if (rows == 2)
      cur
    else {
      val next = Buffer.fill(cur.size)(Buffer[Bool]())
      cur.zipWithIndex.filter(_._1.size > 0).foreach { case (col, i) =>
        val (toCompress, remaining) = col.grouped(3).toSeq.partition(_.size == 3)
        val compressed = toCompress.map { case Seq(a, b, c) =>
          c32(Seq(a, b, c))
        }
        val remained = remaining.headOption.getOrElse(Seq.empty).toSeq
        compressed.foreach { case Seq(sum, carry) =>
          next(i) += sum
          next(i + 1) += carry
        }
        if (rows == 3 && remained.size == 2 && next(i).size > 0) {
          val Seq(sum, carry) = c22(remained)
          next(i) += sum
          next(i + 1) += carry
        } else next(i) ++= remained
      }
      compress(next.map(_.toSeq).toSeq)
    }
  }

  def getTwoOperandsOf(bm: Seq[Seq[Bool]]): Vec[UInt] = VecInit {
    val padded = compress(bm).map(_.padTo(2, false.B))
    padded.transpose.map(_.reverse).map(Cat(_))
  }
}
