package nl.vandebron.fsm

import zio.{UIO, ZIO}
import zio.actors.Actor.Stateful
import zio.actors._

sealed trait Command[+_]
case class Vend() extends Command[Int]
case class Feed(stock: Int) extends Command[Int]

object VendingFsm {
  val stateful = new Stateful[Any, Int, Command] {
    override def receive[A](
        state: Int,
        msg: Command[A],
        context: Context
    ): UIO[(Int, A)] =
      msg match {
        case Feed(value) =>
          val newStock = state + value
          println(s"Fed, new stock is $newStock")
          ZIO.succeed((newStock, newStock))
        case Vend() if state >= 1 =>
          val newStock = state - 1
          println(s"Vended, new stock is $newStock")
          ZIO.succeed((newStock, newStock))
        case Vend() =>
          println("Failed to vend, out of stock")
          ZIO.succeed((0, 0))
      }
  }
}

//sealed trait Command[+_]
//case class DoubleCommand(value: Int) extends Command[Int]
//object A {
//  val stateful = new Stateful[Any, Unit, Command] {
//    override def receive[A](
//        state: Unit,
//        msg: Command[A],
//        context: Context
//    ): UIO[(Unit, A)] =
//      msg match {
//        case DoubleCommand(value) => ZIO.succeed(((), value * 2))
//      }
//  }
//}
