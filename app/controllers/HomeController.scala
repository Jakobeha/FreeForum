package controllers

import javax.inject._

import models._
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{FutureThreadManager, MainContextManager}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(linkedPostDAO: LinkedPostDAO,
                               futureThreadManager: FutureThreadManager,
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

  def newThread: Action[NewThreadBody] = Action.async(parse.form(NewThreadBody.form)) { implicit request =>
    val newThreadBody = request.body
    val profile = mainContextManager.sessionContext.profile
    profile match {
      case Some(someProfile) =>
        val newThreadPost = someProfile.newThreadPostLinked(newThreadBody)
        val insertedThreadPost = linkedPostDAO.insertLinkedPost(newThreadPost)

        insertedThreadPost.map { insertedThreadPost =>
          Logger.logger.debug("Created " + insertedThreadPost.toString)

          Redirect(routes.HomeController.index())
        }

      case None =>
        Future { futureThreadManager.setSessionValue(Some(newThreadBody)) {
          Redirect(routes.LoginController.login())
        } }
    }
  }
}