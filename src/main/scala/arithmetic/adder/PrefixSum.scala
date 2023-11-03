package arithmetic.adder

import chisel3._
import chisel3.experimental.prefix

trait PrefixSum {
  val original: Seq[(Bool, Bool)]
  def associativeOp(elements: Seq[(Bool, Bool)]): (Bool, Bool)
  def getPrefixSum(): Seq[(Bool, Bool)]
}

trait HanCarlsonPrefixSum extends PrefixSum {
  private def layer(elements: Seq[(Bool, Bool)], offset: Int): Seq[(Bool, Bool)] = {
    require((elements.size & 1) == 0)
    require(offset <= log2Up(elements.size) + 1)

    val next = Seq.tabulate(elements.size) { i =>
      if (offset == 1) { // Brent-Kung
        if ((i & 1) == 1) {
          prefix(s"brent_kung_${offset}_${i}_${i - 1}") {
            associativeOp(Seq(elements(i - 1), elements(i)))
          }
        } else {
          elements(i)
        }
      } else if (offset == log2Up(elements.size) + 1) { // Brent-Kung
        if (i > 1 && (i & 1) == 0) {
          prefix(s"brent_kung_${offset}_${i}_${i - 1}") {
            associativeOp(Seq(elements(i - 1), elements(i)))
          }
        } else {
          elements(i)
        }
      } else { // Kogge-Stone
        val n = 1 << (offset - 1)
        if ((i & 1) == 1 && i > n) {
          prefix(s"kogge_stone_${offset}_${i}_${i - n}") {
            associativeOp(Seq(elements(i - n), elements(i)))
          }
        } else {
          elements(i)
        }
      }
    }

    if (offset < log2Up(elements.size) + 1) {
      layer(next, offset + 1)
    } else {
      next
    }
  }

  def getPrefixSum() = layer(original, 1)
}
