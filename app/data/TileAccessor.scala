package data

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Access MBTiles database.
 *
 * User: EBorshch
 *
 * Date: 21.11.12
 */
object TileAccessor {

  def getTile(x: Int, y: Int, z: Int): Option[Array[Byte]] = {

    DB.withConnection {
      implicit c =>

        val tiles = SQL(
          """ select tile_data from tiles
          where zoom_level = {z} and
                tile_column = {y} and
                tile_row = {x} """).
          on("z" -> z, "y" -> y, "x" -> x)().
          collect {
          case Row(Some(image: Array[Byte])) => image
        }

        tiles match {
          case Nil => None
          case x #:: xs => Some(x)
        }
    }
  }


}
