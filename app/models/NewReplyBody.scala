package models

import play.api.data.{Form, Forms}
import play.api.libs.json.{Json, Reads, Writes}

case class NewReplyBody(content: String) {
  def toPostBody(threadId: Long): NewPostBody = NewPostBody(content, threadId)
}

object NewReplyBody {
  val form = Form(Forms.mapping(
    "content" -> Forms.nonEmptyText
  )(NewReplyBody.apply)(NewReplyBody.unapply))
  val reads: Reads[NewReplyBody] = Json.reads[NewReplyBody]
  val writes: Writes[NewReplyBody] = Json.writes[NewReplyBody]
}