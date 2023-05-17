package nl.vandebron.fsm

import nl.vandebron.fsm.VendingFsm.stateful
import zio.Console._
import zio.actors.{ActorSystem, Supervisor}
import zio.{ZIOAppDefault, _}

object FsmApp extends ZIOAppDefault {
  def run = for {
    system <- ActorSystem("mySystem")
    actor <- system.make(
      "actor1",
      Supervisor.none,
      new OutOfStockState(),
      stateful
    )
    _ <- actor ! Feed(4)
    _ <- actor ! Vend()
    _ <- actor ! Vend()
    _ <- actor ! Vend()
    _ <- actor ! Vend()
    _ <- actor ! Vend()
    _ <- actor ! Feed(1)

    stock <- actor ! Vend()
    _ <- printLine("finished")
  } yield stock

}
