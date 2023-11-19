package arithmetic.adder

import chisel3._
import chisel3.util.Cat

/** An abstract adder module.
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

/** An abstract multi-operand adder module.
  *
  * @param n the number of inputs to the adder.
  * @param width the bit width of each input.
  */
abstract class MOAdder(n: Int, width: Int) extends Module {
  val inputs = IO(Input(Vec(n, UInt(width.W))))
  val sum    = IO(Output(UInt()))
  assert(sum === inputs.reduceTree(_ + _))
}
