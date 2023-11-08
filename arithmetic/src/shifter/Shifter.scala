package arithmetic.shift

import chisel3._
import chisel3.util.{Cat, Mux1H, UIntToOH}

object BarrelShifter {
  private trait ShiftType
  private object LeftShift   extends ShiftType
  private object RightShift  extends ShiftType
  private object LeftRotate  extends ShiftType
  private object RightRotate extends ShiftType

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
}
