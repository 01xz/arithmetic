package arithmetic.multiplier

import chisel3._
import chisel3.util.Cat
import arithmetic.compressor._
import arithmetic.util.SignExt

abstract class Multiplier[T <: Data](in: => T, out: => T) extends Module {
  val lhs, rhs = IO(Input(in))
  val product  = IO(Output(out))
}

abstract class BoothMultiplier(width: Int) extends Multiplier(UInt(width.W), UInt()) with Booth with CompressorTree {
  val ls, rs = IO(Input(Bool()))

  val pp     = getPartialProductOf(SignExt(ls, lhs), SignExt(rs, rhs))
  val result = getTwoOperandsOf(pp).reduce(_ + _)

  product := result(width * 2 - 1, 0)

  assert {
    val sLhs     = SignExt(ls, lhs).asSInt
    val sRhs     = SignExt(rs, rhs).asSInt
    val sProduct = result(width * 2 + 1, 0).asSInt
    sProduct === sLhs * sRhs
  }
}

class Radix4BoothWallaceMultiplier(width: Int) extends BoothMultiplier(width) with Radix4Booth with WallaceTree
