package models

import myutils.DateUtils
import play.api.libs.json.{Json, Reads, Writes}

case class Profile(name: String) {
  def newThreadPostLinked(body: NewThreadBody): LinkedPost = LinkedPost(
    post = newThreadPost(body),
    thread = newThread(body)
  )

  def newThread(body: NewThreadBody): Thread = Thread(
    id = None,
    title = body.title,
    author = name,
    createdDate = DateUtils.now()
  )

  def newThreadPost(body: NewThreadBody): Post = Post(
    id = None,
    content = body.content,
    author = name,
    createdDate = DateUtils.now(),
    threadId = None
  )
}

object Profile {
  def apply(login: Login): Profile = Profile(login.name)

  val reads: Reads[Profile] = Json.reads[Profile]
  val writes: Writes[Profile] = Json.writes[Profile]
}
