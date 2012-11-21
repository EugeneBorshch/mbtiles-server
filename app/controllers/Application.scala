package controllers


import play.api.mvc._
import data.TileAccessor

object Application extends Controller {

  def index = Action {
    Ok("Hello ^.^ ")
  }

  def tile(x: Int, y: Int, z: Int) = Action {
    val tile = TileAccessor.getTile(2587, 1206,12)
    println(tile)
    println("Read http://stackoverflow.com/questions/10391987/how-do-i-configure-multiple-databases-work-in-play-2-0")
    Ok("You requested x = " + x + "  y = " + y + "  z = " + z)
  }


}