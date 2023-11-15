package arithmetic.util

import chisel3._
import chisel3.util._

/** Sign extends a UInt number based on a Bool indicating whether the number is signed or not.
  *
  * @param s The Boolean value indicating whether the number is signed or not.
  * @param src The original UInt number to be sign extended.
  * @return A new UInt number with the same value as the original, but with additional bits added to the left to represent the sign.
  */
object SignExt {
  def apply(s: Bool, src: UInt) = {
    val sign = s & src.head(1)
    Cat(sign, src)
  }
}
