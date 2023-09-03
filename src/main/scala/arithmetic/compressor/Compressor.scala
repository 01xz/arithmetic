package arithmetic.compressor

import chisel3._

trait Compressor {
  def c22(in: Seq[Bool]): Seq[Bool] = {
    require(in.size == 2)
    in match {
      case Seq(a, b) => {
        val sum   = a ^ b
        val carry = a & b
        Seq(sum, carry)
      }
    }
  }

  def c32(in: Seq[Bool]): Seq[Bool] = {
    require(in.size == 3)
    in match {
      case Seq(a, b, c) => {
        val xor   = a ^ b
        val sum   = xor ^ c
        val carry = (a & b) | (xor & c)
        Seq(sum, carry)
      }
    }
  }

  def c53(in: Seq[Bool]): Seq[Bool] = {
    require(in.size == 5)
    in match {
      case Seq(a, b, c, d, cin) => {
        val xor0  = a ^ b
        val xor1  = c ^ d
        val xor2  = xor1 ^ xor0
        val cout  = (~xor0 & a) | (xor0 & c);
        val sum   = xor2 ^ cin
        val carry = (~xor2 & d) | (xor2 & cin);
        Seq(sum, carry, cout)
      }
    }
  }
}
