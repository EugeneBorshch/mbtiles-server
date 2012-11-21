package data

/**
 * Tile Query .
 * Each map tile is described with a zoom level and combination of row and column numbers.
 *
 * User: Eugene Borshch
 * Date: 11/22/12
 */

case class TileQuery(col: Int, row: Int, level: Int)
