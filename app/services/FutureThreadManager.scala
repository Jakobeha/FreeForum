package services

import javax.inject.{Inject, Singleton}

import models.{LinkedPostDAO, NewThreadBody, Profile}
import play.api.Logger
import play.api.mvc.{Request, Result}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FutureThreadManager @Inject()(linkedPostDAO: LinkedPostDAO)
                                   (implicit executionContext: ExecutionContext)
  extends SessionValueManager[NewThreadBody]("futureThread")(NewThreadBody.reads, NewThreadBody.writes) {
  def process(profile: Profile)
             (result: Result)
             (implicit request: Request[Any]): Future[Result] = {
    sessionValue match {
      case Some(futureThread) =>
        val newThreadPost = profile.newThreadPostLinked(futureThread)
        val insertedThreadPost = linkedPostDAO.insertLinkedPost(newThreadPost)

        insertedThreadPost.map { insertedThreadPost =>
          Logger.logger.debug(s"Created cached: " + insertedThreadPost.toString)

          result
        }
      case None => Future {
        result
      }
    }
  }
}