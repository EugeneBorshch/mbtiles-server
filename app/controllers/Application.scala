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

  def index = Action {
    Ok("Hello ^.^ ")
  }

  def tile(col: Int, row: Int, level: Int) = Action {

    val myActor = Akka.system.actorOf(Props[TileActor])

    implicit val timeout = Timeout(5 seconds)

    Async {
      (myActor ? TileQuery(col, row, level)).mapTo[Option[Array[Byte]]].asPromise.map {
        response => prepareResponse(response)
      }
    }

  }

  def prepareResponse(response: Option[Array[Byte]]): SimpleResult[Array[Byte]] = {
    response match {
      case None => SimpleResult(
        header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/png")),
        body = Enumerator()
      )
      case Some(s) =>
        SimpleResult(
          header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/png")),
          body = Enumerator(s)
        )
    }
  }

}