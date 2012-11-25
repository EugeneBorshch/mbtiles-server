package controllers


import play.api.mvc._
import data.{TileAccessor, MBTileAccessor, TileQuery}
import play.api.libs.iteratee.Enumerator
import play.api.libs.concurrent._
import actors.TileActor
import play.libs.Akka
import akka.actor.Props
import akka.util.Timeout


import akka.util.duration.intToDurationInt
import akka.pattern.ask
import play.api.mvc.Results.EmptyContent
import akka.dispatch.Future

class TileApplication(tileAccessor: TileAccessor) extends Controller {
  val headers: Map[String, String] = Map(
    CONTENT_TYPE -> "image/png",
    "Access-Control-Allow-Origin" -> "*",
    ALLOW -> "GET"
  )

  /**
   * Default stub action
   */
  def index = Action {
    Ok("Hello ^.^ ")
  }

  /**
   * Main action than serves tile images
   */
  def tile(col: Int, row: Int, level: Int) = Action {

    implicit val timeout = Timeout(5 seconds)

    val tileActor = Akka.system.actorOf(Props(new TileActor(tileAccessor)))

    Async {
      val future = tileActor ? TileQuery(col, row, level)
      val promise = future.mapTo[Option[Array[Byte]]].asPromise
      promise.map(response => processResponse(response))
    }

  }

  def processResponse(response: Option[Array[Byte]]) = {
    response match {
      case None => NoContent
      case Some(s) =>
        SimpleResult(
          header = ResponseHeader(200, headers),
          body = Enumerator(s)
        )
    }
  }
}


object Application extends TileApplication(MBTileAccessor)