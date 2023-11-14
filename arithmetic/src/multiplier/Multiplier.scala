package arithmetic.multiplier

import chisel3._

abstract class Multiplier[T <: Data](in: => T, out: => T) extends Module {
  val lhs, rhs = IO(Input(in))
  val product  = IO(Output(out))
}
