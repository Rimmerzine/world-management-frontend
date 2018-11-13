package helpers

import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait UnitSpec extends PlaySpec with MockitoSugar {

  def await[T](f: Future[T]): T = Await.result(f, Duration("5 seconds"))

}