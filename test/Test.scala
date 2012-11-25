import controllers.TileApplication
import data.{TileQuery, TileAccessor}
import org.specs2.mutable.Specification
import play.api.libs.concurrent.Promise
import play.api.mvc.{PlainResult, Result, AsyncResult}
import play.api.test.{FakeApplication, FakeRequest}
import scala.Array
import play.api.test.Helpers._

/**
 * Tests for the TileApplication controller.
 *
 * User: Eugene Borshch
 */
class Test extends Specification {

  "TileApplication" should {

    "return OK for existing tiles" in {
      running(FakeApplication()) {
        val unknResult = new TileApplication(FakeTileAccessor).tile(1, 1, 1)(FakeRequest())
        val result = getResult(unknResult)
        status(result) must equalTo(OK)
        contentType(result) must equalTo(Some("image/png"))
        contentAsBytes(result).length must be greaterThan 0
      }
    }


    "return NO_CONTENT for tiles that are out of bounds" in {
      running(FakeApplication()) {
        val unknResult = new TileApplication(FakeTileAccessor).tile(2, 2, 2)(FakeRequest())
        val result = getResult(unknResult)
        status(result) must equalTo(NO_CONTENT)
      }
    }

  }

  /**
   * Play 2.0.4 have problems with async testing. Should wait for the new version 2.1
   */
  def getResult(result: Result) = {
    result match {
      case async: AsyncResult => {
        val promise: Promise[Result] = async.result
        promise.value.get
      }
      case plain: PlainResult => plain
    }
  }
}

/**
 * Fake tile accessor
 */
object FakeTileAccessor extends TileAccessor {
  val fakeData = Map(
    TileQuery(1, 1, 1) -> Array[Byte](1)
  )

  def getTile(tile: TileQuery) = {

    val blob = for {fakeRow <- fakeData
                    if tile.col == fakeRow._1.col &&
                      tile.row == fakeRow._1.row &&
                      tile.level == fakeRow._1.level
    } yield fakeRow._2

    blob match {
      case Nil => None
      case x :: xs => Some(x)
    }
  }
}

