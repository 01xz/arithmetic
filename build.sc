import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

val chiselVersion = "5.0.0"

object arithmetic extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.10"
  def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit"
  )
  def ivyDeps = Agg(
    ivy"org.chipsalliance::chisel::$chiselVersion"
  )
  def scalacPluginIvyDeps = Agg(
    ivy"org.chipsalliance:::chisel-plugin::$chiselVersion"
  )
}
