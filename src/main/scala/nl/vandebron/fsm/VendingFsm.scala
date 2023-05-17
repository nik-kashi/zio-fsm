package nl.vandebron.fsm

import zio.{UIO, ZIO}
import zio.actors.Actor.Stateful
import zio.actors._

sealed trait Command[+_]
case class Vend() extends Command[Int]
case class Feed(stock: Int) extends Command[Int]

sealed trait State {
  val stock: Int
  def receiver[A]: Command[A] => UIO[(State, A)]
}

class VendibleState(val stock: Int) extends State {
  override def receiver[A]: Command[A] => UIO[(State, A)] = {
    case Feed(value) =>
      val newStock = stock + value
      println(s"Fed, new stock is $newStock")
      ZIO.succeed((new VendibleState(newStock), newStock))
    case Vend() =>
      val newStock = stock - 1
      println(s"Vended, new stock is $newStock")
      if (newStock > 0)
        ZIO.succeed((new VendibleState(newStock), newStock))
      else
        ZIO.succeed((new OutOfStockState(), newStock))

  }
}

class OutOfStockState extends State {

  override val stock: Int = 0
  override def receiver[A]: Command[A] => UIO[(State, A)] = {
    case Feed(value) =>
      val newStock = stock + value
      println(s"Fed, new stock is $newStock")
      ZIO.succeed((new VendibleState(newStock), newStock))
    case Vend() =>
      println("Failed to vend, out of stock")
      ZIO.succeed((new OutOfStockState(), 0))

  }
}
object VendingFsm {
  val stateful = new Stateful[Any, State, Command] {
    override def receive[A](
        state: State,
        msg: Command[A],
        context: Context
    ): UIO[(State, A)] =
      state.receiver[A](msg)

  }
}
