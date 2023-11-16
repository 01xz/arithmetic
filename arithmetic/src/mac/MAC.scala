package arithmetic.mac

import chisel3._

/** An abstract Multiply-Accumulator (MAC) module.
  * @param mWidth The bit width of the signals to be multiplied.
  * @param aWidth The bit width of the signal to accumulated.
  */
abstract class MAC(mWidth: Int, aWidth: Int) extends Module {
  val a, b   = IO(Input(UInt(mWidth.W)))
  val s      = IO(Input(UInt(aWidth.W)))
  val result = IO(Output(UInt()))
}
