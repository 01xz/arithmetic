package arithmetic.adder

import chisel3._

abstract class Adder(width: Int) extends Module {
  val io = IO(new Bundle {
    val lhs, rhs = Input(UInt(width.W))
    val cin      = Input(Bool())
    val sum      = Output(UInt(width.W))
    val cout     = Output(Bool())
  })
}
