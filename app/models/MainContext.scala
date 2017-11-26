package models

case class MainContext(errors: Seq[String],
                       profile: Option[Profile],
                       futureThread: Option[NewThreadBody]) {

}
