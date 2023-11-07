package arithmetic.multiplier

import chisel3._
import chisel3.util._

trait Booth {
  val lhs: UInt
  val rhs: UInt

  def getPartialProduct(): Seq[Seq[Bool]] = {
    val signExt   = Cat(lhs.head(1), lhs)
    val double    = Cat(lhs, false.B)
    val neg       = ~signExt
    val negDouble = Cat(neg.tail(1), false.B)

    val rows      = (rhs.getWidth + 2) / 2
    val rhsPadded = Wire(UInt((rows * 2 + 1).W))

    rhsPadded := Cat(rhs, false.B)

    val pp = Seq.tabulate(rows) { i =>
      val curMatched = rhsPadded(i * 2 + 2, i * 2)
      val preMatched =
        if (i == 0) 0.U(3.W)
        else rhsPadded(i * 2, i * 2 - 2)

      val encoded = MuxLookup(curMatched, 0.U) {
        Seq(
          1.U -> signExt,
          2.U -> signExt,
          3.U -> double,
          4.U -> negDouble,
          5.U -> neg,
          6.U -> neg
        )
      }

      val correction = MuxLookup(preMatched, 0.U) {
        Seq(
          4.U -> 2.U(2.W),
          5.U -> 1.U(2.W),
          6.U -> 1.U(2.W)
        )
      }

      val sign = encoded.head(1)

      if (i == 0) Cat(~sign, sign, sign, encoded)
      else if (i == rows - 1) Cat(~sign, encoded, correction)
      else Cat(true.B, ~sign, encoded, correction)
    }

    def weight(i: Int) = if (i == 0) 0 else (i - 1) * 2

    val cols = weight(pp.size - 1) + pp.last.getWidth
    Seq.tabulate(cols) { i =>
      (0 until pp.size).collect {
        case j if (i - weight(j) >= 0 && i - weight(j) < pp(j).getWidth) => pp(j)(i - weight(j))
      }.toSeq
    }
  }
}
