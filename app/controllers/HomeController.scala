package controllers

import javax.inject._

import models._
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{FuturePostManager, FutureThreadManager, MainContextManager}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(linkedPostDAO: LinkedPostDAO,
                               threadDAO: ThreadDAO,
                               postDAO: PostDAO,
                               futureThreadManager: FutureThreadManager,
                               futurePostManager: FuturePostManager,
                               mainContextManager: MainContextManager,
                               cc: ControllerComponents)
                              (implicit executionContext: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {
  def index: Action[AnyContent] = Action.async { implicit request =>
    implicit val mainContext: MainContext = mainContextManager.sessionContext
    for {
      threadPosts <- linkedPostDAO.allThreadPosts
    } yield Ok(views.html.index(threadPosts))
  }

  def viewThreadById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    implicit val mainContext: MainContext = mainContextManager.sessionContext
    for {
      thread <- threadDAO.withId(id)
      posts <- postDAO.inThreadWithId(id)
    } yield Ok(views.html.thread(thread, posts))
  }

  def viewThreadByTitle(title: String): Action[AnyContent] = Action.async { implicit request =>
    implicit val mainContext: MainContext = mainContextManager.sessionContext
    for {
      thread <- threadDAO.withTitle(title)
      posts <- postDAO.inThreadWithId(thread.flatMap(_.id).getOrElse(-1L))
    } yield Ok(views.html.thread(thread, posts))
  }

  def newThread: Action[NewThreadBody] = Action.async(parse.form(NewThreadBody.form)) { implicit request =>
    val newThreadBody = request.body
    val profile = mainContextManager.sessionContext.profile
    profile match {
      case Some(someProfile) =>
        val newThreadPost = someProfile.newThreadPostLinked(newThreadBody)
        val insertedThreadPost = linkedPostDAO.insertLinkedPost(newThreadPost)

        insertedThreadPost.map { insertedThreadPost =>
          Logger.logger.debug("Created thread post " + insertedThreadPost.toString)

          Redirect(routes.HomeController.index())
        }

      case None =>
        Future { futureThreadManager.setSessionValue(Some(newThreadBody)) {
          Redirect(routes.LoginController.login())
        } }
    }
  }

  def newReply(threadId: Long): Action[NewReplyBody] = Action.async(parse.form(NewReplyBody.form)) { implicit request =>
    val newReplyBody = request.body
    val profile = mainContextManager.sessionContext.profile
    profile match {
      case Some(someProfile) =>
        val newReply = someProfile.newReplyPost(newReplyBody, threadId)
        val insertedReply = postDAO.insert(newReply)

        insertedReply.map { insertedReply =>
          Logger.logger.debug("Created reply " + insertedReply.toString)

          Redirect(routes.HomeController.index())
        }

      case None =>
        val newPostBody = newReplyBody.toPostBody(threadId)
        Future { futurePostManager.setSessionValue(Some(newPostBody)) {
          Redirect(routes.LoginController.login())
        } }
    }
  }
}