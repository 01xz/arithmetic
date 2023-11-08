package arithmetic.fp

import chisel3._

object RoundingMode {
  def RNE = "b000".U(3.W)
  def RTZ = "b001".U(3.W)
  def RDN = "b010".U(3.W)
  def RUP = "b011".U(3.W)
  def RMM = "b100".U(3.W)
}
