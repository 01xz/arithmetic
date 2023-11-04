package arithmetic.adder

import chisel3._
import chisel3.util.Cat
import arithmetic.adder.PrefixSum

abstract class PrefixAdder(width: Int) extends Adder(width) with PrefixSum {
  val cin = io.cin
  val original = Seq.tabulate(width) { i =>
    val generate  = dontTouch(io.lhs(i) & io.rhs(i)).suggestName(s"original_0_${i}_${i}_g")
    val propagate = dontTouch(io.lhs(i) ^ io.rhs(i)).suggestName(s"original_0_${i}_${i}_p")
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

  val prefixSum = getPrefixSum()

  val carrys = cin +: Seq.tabulate(width) { i =>
    prefixSum(i) match {
      case (g: Bool, p: Bool) => g | (p & cin)
    }
  }

  val sum = VecInit
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

  io.cout := sum(width)
  io.sum  := sum(width - 1, 0)
}
