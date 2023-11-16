package arithmetic.adder

import chisel3._
import chisel3.util.Cat

/** An abstract class representing an adder module.
  *
  * @param width The bit width of the adder.
  */
abstract class Adder(width: Int) extends Module {
  val lhs, rhs = IO(Input(UInt(width.W)))
  val cin      = IO(Input(Bool()))
  val sum      = IO(Output(UInt(width.W)))
  val cout     = IO(Output(Bool()))
  assert(Cat(cout, sum) === lhs +& rhs + cin)
}
