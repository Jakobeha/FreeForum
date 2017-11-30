package controllers

import javax.inject._

import models._
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.filters.csrf.CSRF
import services.{FuturePostManager, FutureThreadManager, MainContextManager, ProfileManager}

import scala.concurrent.ExecutionContext

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class LoginController @Inject()(linkedPostDAO: LinkedPostDAO,
                                profileManager: ProfileManager,
                                futureThreadManager: FutureThreadManager,
                                futurePostManager: FuturePostManager,
                                mainContextManager: MainContextManager,
                                cc: ControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {
  def login: Action[AnyContent] = Action { implicit request =>
    implicit val mainContext: MainContext = mainContextManager.sessionContext
    Ok(views.html.login())
  }

  def submitLogin: Action[Login] = Action.async(parse.form(Login.form)) { implicit request =>
    val login = request.body
    val newProfile = Profile(login)
    profileManager.setSessionValueAsync(Some(newProfile)) {
      futureThreadManager.processAsync(newProfile) {
        futurePostManager.process(newProfile) {
          Redirect(routes.HomeController.index())
        }
      }
    }
  }

  def logout: Action[AnyContent] = Action { implicit request =>
    profileManager.setSessionValue(None) {
      Redirect(routes.HomeController.index())
    }
  }
}
