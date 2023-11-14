package arithmetic.divider

import chisel3._

abstract class Divider[T <: Data](gen: => T) extends Module {
  val lhs, rhs      = IO(Input(gen))
  val quotient, rem = IO(Output(gen))
}
