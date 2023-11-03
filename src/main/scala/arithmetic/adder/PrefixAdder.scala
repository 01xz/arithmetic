package arithmetic.adder

import chisel3._
import arithmetic.adder.PrefixSum

trait PrefixAdder extends PrefixSum {
  val lhs: UInt
  val rhs: UInt
  val cin: Bool

  val original = Seq.tabulate(width) { i =>
    val generate  = dontTouch(lhs(i) & rhs(i)).suggestName(s"original_0_${i}_${i}_g")
    val propagate = dontTouch(lhs(i) ^ rhs(i)).suggestName(s"original_0_${i}_${i}_p")
    (generate, propagate)
  }

  def associativeOp(elements: Seq[(Bool, Bool)]): (Bool, Bool) = {
    require(elements.size == 2)
    elements match {
      case Seq((g0: Bool, p0: Bool), (g1: Bool, p1: Bool)) => {
        val g = dontTouch(g1 | (g0 & p1))
        val p = dontTouch(p1 & p0)
        (g, p)
      }
    }
  }

  def getSum(): UInt = {
    val prefixSum = getPrefixSum()

    val carrys = cin +: Seq.tabulate(width) { i =>
      prefixSum(i) match {
        case (g: Bool, p: Bool) => g | (p & cin)
      }
    }

    VecInit
    .tabulate(carrys.size) { i =>
      if (i < carrys.size - 1) {
        original(i) match {
          case (_: Bool, p: Bool) => p ^ carrys(i)
        }
      } else {
        carrys(i)
      }
    }
    .asUInt
  }
}
