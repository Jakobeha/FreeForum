package models

import play.api.libs.json.{Json, Reads, Writes}

case class NewPostBody(content: String,
                       threadId: Long) {

}

object NewPostBody {
  val reads: Reads[NewPostBody] = Json.reads[NewPostBody]
  val writes: Writes[NewPostBody] = Json.writes[NewPostBody]
}