package arithmetic.shift

import chisel3._
import chisel3.util.{log2Up, Cat, Fill, Mux1H, UIntToOH}

object BarrelShifter {
  trait ShiftType
  object LeftShift   extends ShiftType
  object RightShift  extends ShiftType
  object LeftRotate  extends ShiftType
  object RightRotate extends ShiftType

  def apply[T <: Data](inputs: Vec[T], shamt: UInt, shiftType: ShiftType, shiftGranularity: Int = 1): Vec[T] = {
    require(shiftGranularity > 0)
    val elementType = chiselTypeOf(inputs.head)
    shamt.asBools.grouped(shiftGranularity).map(Cat(_)).zipWithIndex.foldLeft(inputs) {
      case (prev, (shiftBits, layerIndex)) =>
        Mux1H(
          UIntToOH(shiftBits),
          Seq.tabulate(1 << shiftBits.getWidth) { i =>
            val layerShift: Int = (i * (1 << (layerIndex * shiftGranularity))).min(prev.length)
            VecInit(shiftType match {
              case LeftShift =>
                prev.drop(layerShift) ++ Seq.fill(layerShift)(0.U.asTypeOf(elementType))
              case RightShift =>
                Seq.fill(layerShift)(0.U.asTypeOf(elementType)) ++ prev.dropRight(layerShift)
              case LeftRotate =>
                prev.drop(layerShift) ++ prev.take(layerShift)
              case RightRotate =>
                prev.takeRight(layerShift) ++ prev.dropRight(layerShift)
            })
          }
        )
    }
  }

  def apply(input: UInt, shamt: UInt, lr: Bool, la: Bool): UInt = {
    require(shamt.getWidth <= log2Up(input.getWidth))
    shamt.asBools.zipWithIndex.foldLeft(input) { case (prev, (doShift, index)) =>
      val sign                               = input.head(1) & la
      val layerShift                         = 1 << index
      val (leftShift, rightShift, doNothing) = (~lr & doShift, lr & doShift, ~doShift)
      Mux1H(
        Seq(
          leftShift  -> Cat(prev.tail(layerShift), Fill(layerShift, false.B)),
          rightShift -> Cat(Fill(layerShift, sign), prev.head(prev.getWidth - layerShift)),
          doNothing  -> prev
        )
      )
    }
  }
}
