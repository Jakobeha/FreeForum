package services

import java.net.{URLDecoder, URLEncoder}

import play.api.libs.json._
import play.api.mvc.{Cookie, DiscardingCookie, Request, Result}

import scala.concurrent.{ExecutionContext, Future}

class SessionValueManager[T](label: String)(implicit private val valueReads: Reads[T],
                                            private val valueWrites: Writes[T]) {
  def sessionValueWithErrors(implicit request: Request[Any]): (Option[T], Seq[(JsPath, Seq[JsonValidationError])]) = {
    request.cookies.get(label).map(valueFromCookie) match {
      case None => (None, Seq.empty)
      case Some(JsSuccess(value, _)) => (Some(value), Seq.empty)
      case Some(JsError(errors)) => (None, errors)
    }
  }

  def sessionValue(implicit request: Request[Any]): Option[T] = sessionValueWithErrors._1

  def setSessionValue(newValue: Option[T])
                     (result: Result)
                     (implicit request: Request[Any]): Result = newValue match {
    case Some(someNewValue) => result.withCookies(valueToCookie(someNewValue))
    case None => result.discardingCookies(noValueToCookie)
  }

  def setSessionValueAsync(newValue: Option[T])
                          (result: Future[Result])
                          (implicit request: Request[Any],
                           executionContext: ExecutionContext): Future[Result] = newValue match {
    case Some(someNewValue) => result.map(_.withCookies(valueToCookie(someNewValue)))
    case None => result.map(_.discardingCookies(noValueToCookie))
  }

  private def valueFromCookie(cookie: Cookie): JsResult[T] = {
    Json.fromJson(Json.parse(URLDecoder.decode(cookie.value, "UTF-8")))
  }
  private def valueToCookie(data: T) = Cookie(
    name = label,
    value = URLEncoder.encode(Json.toJson(data).toString, "UTF-8")
  )
  private def noValueToCookie = DiscardingCookie("profile")
}
