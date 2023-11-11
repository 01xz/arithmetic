package arithmetic.butterfly

import chisel3._
import arithmetic.formal._
import chiseltest.ChiselScalatestTester
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec

class ButterflyTest extends AnyFlatSpec with ChiselScalatestTester with Formal with FormalBackendOption {
  "Test Butterfly 4" should "PASS" in {
    val a, b, c, d = UInt(8.W)
    verifyButterflyWith(Seq(a, b, c, d)) { output =>
      assert(output === Seq(d, c, b, a))
    }
  }

  private def verifyButterflyWith[T <: Data](input: Seq[T])(verifyFn: Seq[T] => Unit): Unit = {
    verify(
      new Module {
        val output = Butterfly(input)(_.swap)
        verifyFn(output)
      },
      Seq(BoundedCheck(1), DefaultBackend)
    )
  }
}
