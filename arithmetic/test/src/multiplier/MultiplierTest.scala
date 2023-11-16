package arithmetic.multiplier

import arithmetic.formal._
import chiseltest.ChiselScalatestTester
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec

class MultiplierTest extends AnyFlatSpec with ChiselScalatestTester with Formal with FormalBackendOption {
  "Test Radix 4 Booth Wallace Multiplier 8" should "PASS" taggedAs FormalTag in {
    verify(new Radix4BoothWallaceMultiplier(8), Seq(BoundedCheck(1), DefaultBackend))
  }
}
