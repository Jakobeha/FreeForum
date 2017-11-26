package services

import javax.inject.{Inject, Singleton}

import models.MainContext
import play.api.mvc.Request

@Singleton
class MainContextManager @Inject()(profileManager: ProfileManager,
                                   futureThreadManager: FutureThreadManager) {
  def sessionContext(implicit request: Request[Any]): MainContext = {
    sessionContext(prevErrors = Seq.empty)
  }

  def sessionContext(prevErrors: Seq[String])
                    (implicit request: Request[Any]): MainContext = {
    val errors = prevErrors.toBuffer

    val (profile, profileErrors) = profileManager.sessionValueWithErrors
    errors ++= profileErrors.map { "Error getting profile: " + _ }

    val (futureThread, futureThreadErrors) = futureThreadManager.sessionValueWithErrors
    errors ++= futureThreadErrors.map { "Error getting cached new thread: " + _ }

    MainContext(errors, profile, futureThread)
  }
}
