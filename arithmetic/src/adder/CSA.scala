package arithmetic.adder

import chisel3._
import chisel3.util.Cat
import chisel3.experimental.prefix
import arithmetic.compressor.Compressor

abstract class CarrySaveAdder(m: Int, n: Int)(width: Int) extends Module {
  val io = IO(new Bundle {
    val in  = Input(Vec(m, UInt(width.W)))
    val out = Output(Vec(n, UInt(width.W)))
  })

  def compressor(in: Seq[Bool]): Seq[Bool]

  val compressed = Seq.tabulate(width) { i =>
    prefix(s"${i}") {
      compressor(io.in.map(_(i)))
    }
  }

  io.out := VecInit(compressed.transpose.map(_.reverse).map(Cat(_)))
}

class CSA22(width: Int) extends CarrySaveAdder(2, 2)(width) with Compressor {
  def compressor(in: Seq[Bool]) = c22(in)
}

class CSA32(width: Int) extends CarrySaveAdder(3, 2)(width) with Compressor {
  def compressor(in: Seq[Bool]) = c32(in)
}

class CSA53(width: Int) extends CarrySaveAdder(5, 3)(width) with Compressor {
  def compressor(in: Seq[Bool]) = c53(in)
}
