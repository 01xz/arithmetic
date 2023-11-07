package arithmetic.adder

import chisel3._
import chisel3.util.log2Up
import chisel3.experimental.prefix

trait PrefixSum {
  val original: Seq[(Bool, Bool)]
  def associativeOp(elements: Seq[(Bool, Bool)]): (Bool, Bool)
  def getPrefixSum(): Seq[(Bool, Bool)]
}

trait LadnerFischerPrefixSum extends PrefixSum {
  private def layer(elements: Seq[(Bool, Bool)], offset: Int): Seq[(Bool, Bool)] = {
    val maxOffset = log2Up(elements.size)
    require((elements.size & 1) == 0)
    require(offset <= maxOffset)

    val next = Seq.tabulate(elements.size) { i =>
      val n = i >> (offset - 1)
      if ((n & 1) == 1) {
        val f = (n << (offset - 1)) - 1
        associativeOp(Seq(elements(f), elements(i)))
      } else {
        elements(i)
      }
    }

    if (offset < maxOffset) {
      layer(next, offset + 1)
    } else {
      next
    }
  }

  def getPrefixSum() = layer(original, 1)
}

trait KoggeStonePrefixSum extends PrefixSum {
  private def layer(elements: Seq[(Bool, Bool)], offset: Int): Seq[(Bool, Bool)] = {
    val maxOffset = log2Up(elements.size)
    require((elements.size & 1) == 0)
    require(offset <= maxOffset)

    val next = Seq.tabulate(elements.size) { i =>
      val n = 1 << (offset - 1)
      if (i < n) {
        elements(i)
      } else {
        associativeOp(Seq(elements(i - n), elements(i)))
      }
    }

    if (offset < maxOffset) {
      layer(next, offset + 1)
    } else {
      next
    }
  }

  def getPrefixSum() = layer(original, 1)
}

trait BrentKungPrefixSum extends PrefixSum {
  private def layer(elements: Seq[(Bool, Bool)], offset: Int): Seq[(Bool, Bool)] = {
    val maxOffset = log2Up(elements.size) * 2 - 1
    require((elements.size & 1) == 0)
    require(offset <= maxOffset)

    val next = Seq.tabulate(elements.size) { i =>
      if (offset > log2Up(elements.size)) {
        val n = log2Up(elements.size) * 2 - offset
        val m = (1 << n) - 1
        if (i > m && ((i - (1 << (n - 1))) & m) == m) {
          val f = i - (1 << (n - 1))
          associativeOp(Seq(elements(f), elements(i)))
        } else {
          elements(i)
        }
      } else {
        val m = (1 << offset) - 1
        if ((i & m) == m) {
          val f = i - (1 << (offset - 1))
          associativeOp(Seq(elements(f), elements(i)))
        } else {
          elements(i)
        }
      }
    }

    if (offset < maxOffset) {
      layer(next, offset + 1)
    } else {
      next
    }
  }

  def getPrefixSum() = layer(original, 1)
}

trait HanCarlsonPrefixSum extends PrefixSum {
  private def layer(elements: Seq[(Bool, Bool)], offset: Int): Seq[(Bool, Bool)] = {
    val maxOffset = log2Up(elements.size) + 1
    require((elements.size & 1) == 0)
    require(offset <= maxOffset)

    val next = Seq.tabulate(elements.size) { i =>
      offset match {
        case 1 if (i & 1) == 1 => // Brent-Kung
          prefix(s"brent_kung_${offset}_${i}_${i - 1}") {
            associativeOp(Seq(elements(i - 1), elements(i)))
          }
        case maxOffset if i > 1 && (i & 1) == 0 => // Brent-Kung
          prefix(s"brent_kung_${offset}_${i}_${i - 1}") {
            associativeOp(Seq(elements(i - 1), elements(i)))
          }
        case _ =>
          val n = 1 << (offset - 1)
          if ((i & 1) == 1 && i > n) { // Kogge-Stone
            prefix(s"kogge_stone_${offset}_${i}_${i - n}") {
              associativeOp(Seq(elements(i - n), elements(i)))
            }
          } else {
            elements(i)
          }
      }
    }

    if (offset < maxOffset) {
      layer(next, offset + 1)
    } else {
      next
    }
  }

  def getPrefixSum() = layer(original, 1)
}
