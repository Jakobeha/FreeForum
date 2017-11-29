package services

import javax.inject.{Inject, Singleton}

import models.{NewPostBody, PostDAO, Profile}
import play.api.Logger
import play.api.mvc.{Request, Result}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FuturePostManager @Inject()(postDAO: PostDAO)
                                 (implicit executionContext: ExecutionContext)
  extends SessionValueManager[NewPostBody]("futurePost")(NewPostBody.reads, NewPostBody.writes) {
  def process(profile: Profile)
             (result: Result)
             (implicit request: Request[Any]): Future[Result] = {
    processGen(profile)(result)
  }

  def processAsync(profile: Profile)
                  (result: Future[Result])
                  (implicit request: Request[Any]): Future[Result] = {
    processGen(profile)(result).flatten
  }

  def processGen[Res](profile: Profile)
                     (result: Res)
                     (implicit request: Request[Any]): Future[Res] = {
    sessionValue match {
      case Some(futurePost) =>
        val newPost = profile.newPost(futurePost)
        val insertedPost = postDAO.insert(newPost)

        insertedPost.map { insertedPost =>
          Logger.logger.debug(s"Created cached post: " + insertedPost.toString)

          result
        }
      case None => Future {
        result
      }
    }
  }
}