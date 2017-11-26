package models

import play.api.data.{Form, Forms}
import play.api.libs.json.{Json, Reads, Writes}

case class NewThreadBody(title: String,
                         content: String) {

}

object NewThreadBody {
  val maxTitleLength = 128

  val form = Form(Forms.mapping(
    "title" -> Forms.nonEmptyText(minLength = 1, maxLength = maxTitleLength),
    "content" -> Forms.nonEmptyText
  )(NewThreadBody.apply)(NewThreadBody.unapply))
  val reads: Reads[NewThreadBody] = Json.reads[NewThreadBody]
  val writes: Writes[NewThreadBody] = Json.writes[NewThreadBody]
}