package arithmetic.adder

import arithmetic.formal._
import chiseltest.ChiselScalatestTester
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec

class PrefixAdderTest extends AnyFlatSpec with ChiselScalatestTester with Formal with FormalBackendOption {
  "Test Ladner Fischer Prefix Adder 8" should "PASS" taggedAs FormalTag in {
    verify(new LadnerFischerPrefixAdder(8), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Ladner Fischer Prefix Adder 16" should "PASS" taggedAs FormalTag in {
    verify(new LadnerFischerPrefixAdder(16), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Ladner Fischer Prefix Adder 32" should "PASS" taggedAs FormalTag in {
    verify(new LadnerFischerPrefixAdder(32), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Ladner Fischer Prefix Adder 64" should "PASS" taggedAs FormalTag in {
    verify(new LadnerFischerPrefixAdder(64), Seq(BoundedCheck(1), DefaultBackend))
  }

  "Test Kogge Stone Prefix Adder 8" should "PASS" taggedAs FormalTag in {
    verify(new KoggeStonePrefixAdder(8), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Kogge Stone Prefix Adder 16" should "PASS" taggedAs FormalTag in {
    verify(new KoggeStonePrefixAdder(16), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Kogge Stone Prefix Adder 32" should "PASS" taggedAs FormalTag in {
    verify(new KoggeStonePrefixAdder(32), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Kogge Stone Prefix Adder 64" should "PASS" taggedAs FormalTag in {
    verify(new KoggeStonePrefixAdder(64), Seq(BoundedCheck(1), DefaultBackend))
  }

  "Test Brent Kung Prefix Adder 8" should "PASS" taggedAs FormalTag in {
    verify(new BrentKungPrefixAdder(8), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Brent Kung Prefix Adder 16" should "PASS" taggedAs FormalTag in {
    verify(new BrentKungPrefixAdder(16), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Brent Kung Prefix Adder 32" should "PASS" taggedAs FormalTag in {
    verify(new BrentKungPrefixAdder(32), Seq(BoundedCheck(1), DefaultBackend))
  }
  "Test Brent Kung Prefix Adder 64" should "PASS" taggedAs FormalTag in {
    verify(new BrentKungPrefixAdder(64), Seq(BoundedCheck(1), DefaultBackend))
  }

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
