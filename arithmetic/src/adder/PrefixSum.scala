package arithmetic.adder

import chisel3._
import chisel3.util.log2Up
import chisel3.experimental.prefix

trait PrefixSum {
  def associativeOp(elements: Seq[(Bool, Bool)]): (Bool, Bool)
  def getPrefixSumOf(orig: Seq[(Bool, Bool)]): Seq[(Bool, Bool)]
}

trait LadnerFischerPrefixSum extends PrefixSum {
  def getPrefixSumOf(orig: Seq[(Bool, Bool)]): Seq[(Bool, Bool)] = {
    (1 to log2Up(orig.size)).foldLeft(orig) { case (prev, index) =>
      Seq.tabulate(prev.size) { i =>
        val n = i >> (index - 1)
        if ((n & 1) == 1) {
          val f = (n << (index - 1)) - 1
          prefix(s"ladner_fischer_${index}_${i}_${f}") {
            associativeOp(Seq(prev(f), prev(i)))
          }
        } else prev(i)
      }
    }
  }
}

trait KoggeStonePrefixSum extends PrefixSum {
  def getPrefixSumOf(orig: Seq[(Bool, Bool)]): Seq[(Bool, Bool)] = {
    (1 to log2Up(orig.size)).foldLeft(orig) { case (prev, index) =>
      Seq.tabulate(prev.size) { i =>
        val n = 1 << (index - 1)
        if (i < n) prev(i)
        else
          prefix(s"kogge_stone_${index}_${i}_${i - n}") {
            associativeOp(Seq(prev(i - n), prev(i)))
          }
      }
    }
  }
}

trait BrentKungPrefixSum extends PrefixSum {
  def getPrefixSumOf(orig: Seq[(Bool, Bool)]): Seq[(Bool, Bool)] = {
    val maxIndex = log2Up(orig.size) * 2 - 1
    (1 to maxIndex).foldLeft(orig) { case (prev, index) =>
      Seq.tabulate(prev.size) { i =>
        index match {
          case j if j > log2Up(orig.size) =>
            val n = log2Up(orig.size) * 2 - j
            val m = (1 << n) - 1
            if (i > m && ((i - (1 << (n - 1))) & m) == m) {
              val f = i - (1 << (n - 1))
              prefix(s"brent_kung_${index}_${i}_${f}") {
                associativeOp(Seq(prev(f), prev(i)))
              }
            } else prev(i)
          case _ =>
            val m = (1 << index) - 1
            if ((i & m) == m) {
              val f = i - (1 << (index - 1))
              prefix(s"brent_kung_${index}_${i}_${f}") {
                associativeOp(Seq(prev(f), prev(i)))
              }
            } else prev(i)
        }
      }
    }
  }
}

trait HanCarlsonPrefixSum extends PrefixSum {
  def getPrefixSumOf(orig: Seq[(Bool, Bool)]): Seq[(Bool, Bool)] = {
    val maxIndex = log2Up(orig.size) + 1
    (1 to maxIndex).foldLeft(orig) { case (prev, index) =>
      Seq.tabulate(prev.size) { i =>
        index match {
          case 1 if (i & 1) == 1 => // Brent-Kung
            prefix(s"brent_kung_${index}_${i}_${i - 1}") {
              associativeOp(Seq(prev(i - 1), prev(i)))
            }
          case j if j == maxIndex && i > 1 && (i & 1) == 0 => // Brent-Kung
            prefix(s"brent_kung_${index}_${i}_${i - 1}") {
              associativeOp(Seq(prev(i - 1), prev(i)))
            }
          case _ =>
            val n = 1 << (index - 1)
            if ((i & 1) == 1 && i > n) // Kogge-Stone
              prefix(s"kogge_stone_${index}_${i}_${i - n}") {
                associativeOp(Seq(prev(i - n), prev(i)))
              }
            else prev(i)
        }
      }
    }
  }
}
