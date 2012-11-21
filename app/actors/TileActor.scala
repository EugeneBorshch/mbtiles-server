package actors

import akka.actor.Actor
import data.{TileAccessor, TileQuery}


/**
 * Akka actor that will serve map tile images from MBTiles SqLite database
 *
 * User: Eugene Borshch
 * Date: 11/21/12
 */
class TileActor extends Actor {


  def receive = {

    // if we've received TileQuery
    case tileQuery: TileQuery => {

      //query database for tile image
      val tile = TileAccessor.getTile(tileQuery)
      sender ! tile
    }
    // if we've received bad request
    case _ => sender ! None
  }


}
