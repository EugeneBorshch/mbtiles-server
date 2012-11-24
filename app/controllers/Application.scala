package controllers


import play.api.mvc._
import data.TileQuery
import play.api.libs.iteratee.Enumerator
import play.api.libs.concurrent._
import actors.TileActor
import play.libs.Akka
import akka.actor.Props
import akka.util.Timeout


import akka.util.duration.intToDurationInt
import akka.pattern.ask


object Application extends Controller {

  val headers: Map[String, String] = Map(
    CONTENT_TYPE -> "image/png",
    "Access-Control-Allow-Origin" -> "*",
    ALLOW -> "GET"
  )

  def index = Action {
    Ok("Hello ^.^ ")
  }

  def tile(col: Int, row: Int, level: Int) = Action {

    implicit val timeout = Timeout(5 seconds)

    val tileActor = Akka.system.actorOf(Props[TileActor])

    Async {
      (tileActor ? TileQuery(col, row, level)).mapTo[Option[Array[Byte]]].asPromise.map {
        response => prepareResponse(response)
      }
    }

  }

  def prepareResponse(response: Option[Array[Byte]]): SimpleResult[Array[Byte]] = {

    response match {
      case None => SimpleResult(
        header = ResponseHeader(200, headers),
        body = Enumerator()
      )
      case Some(s) =>
        SimpleResult(
          header = ResponseHeader(200, headers),
          body = Enumerator(s)
        )
    }
  }

}