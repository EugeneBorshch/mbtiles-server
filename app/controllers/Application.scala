package controllers


import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("Hello ^.^ ")
  }

  def tile(x: Int, y: Int, z: Int) = Action {
    Ok("You requested x = " + x + "  y = " + y + "  z = " + z)
  }


}