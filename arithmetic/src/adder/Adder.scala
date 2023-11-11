package arithmetic.adder

import chisel3._

abstract class Adder(width: Int) extends Module {
  val lhs, rhs = IO(Input(UInt(width.W)))
  val cin      = IO(Input(Bool()))
  val sum      = IO(Output(UInt(width.W)))
  val cout     = IO(Output(Bool()))
}
