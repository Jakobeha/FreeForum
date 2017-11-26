package models

import play.api.data.{Form, Forms}

case class Login(name: String) {

}

object Login {
  val form = Form(Forms.mapping(
    "name" -> Forms.nonEmptyText
  )(Login.apply)(Login.unapply))
}