package arithmetic.adder

import arithmetic.formal._
import chiseltest.ChiselScalatestTester
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec

class PrefixAdderTest extends AnyFlatSpec with ChiselScalatestTester with Formal with FormalBackendOption {
  "Test Han Carlson Prefix Adder 8" should "PASS" taggedAs FormalTag in {
    verify(new HanCarlsonPrefixAdder(8), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Han Carlson Prefix Adder 16" should "PASS" taggedAs FormalTag in {
    verify(new HanCarlsonPrefixAdder(16), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Han Carlson Prefix Adder 32" should "PASS" taggedAs FormalTag in {
    verify(new HanCarlsonPrefixAdder(32), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Han Carlson Prefix Adder 64" should "PASS" taggedAs FormalTag in {
    verify(new HanCarlsonPrefixAdder(64), Seq(BoundedCheck(1), DefaultBackend))
  }
}
