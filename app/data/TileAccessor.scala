package data

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Access MBTiles SqLite database.
 *
 * User: Eugene Borshch
 *
 * Date: 21.11.12
 */
object TileAccessor {

  /**
   * Query database to get tile image.
   * @param tile - tile identifier
   * @return
   */
  def getTile(tile: TileQuery): Option[Array[Byte]] = {

    DB.withConnection {
      implicit c =>

      //create sql query to get tile image
        val sql: String = """
                    select tile_data from tiles
                    where zoom_level = {level} and
                        tile_column = {col} and
                        tile_row = {row} """

        val sqlQuery: SimpleSql[Row] = SQL(sql).on("level" -> tile.level, "row" -> tile.row, "col" -> tile.col)

        //execute query
        val tiles = sqlQuery().collect {
          case Row(Some(image: Array[Byte])) => image
        }

        //return image if any
        tiles match {
          case head #:: xs => Some(head)
          case _ => None
        }
    }
  }


}
