package bootstrap

import java.util.GregorianCalendar
import javax.inject.Inject

import com.google.inject.AbstractModule
import models._
import play.api.Logger

import scala.concurrent.ExecutionContext

class LinkedPostsModule extends AbstractModule {
  override protected def configure(): Unit = {
    bind(classOf[LinkedPostsModule.InitialData]).asEagerSingleton()
  }
}

object LinkedPostsModule {
  private class InitialData @Inject()(threadDAO: ThreadDAO,
                                      postDAO: PostDAO)
                                     (implicit executionContext: ExecutionContext) {
    threadDAO.lazyLoad {
      Logger.logger.debug("Adding initial threads")

      InitialData.threads
    }

    postDAO.lazyLoad {
      Logger.logger.debug("Adding initial posts")

      InitialData.posts
    }
  }

  private object InitialData {
    def threads = Seq(
      Thread(
        id = Some(1L),
        title = "Test thread #1",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 11, 19).getTime
      ),
      Thread(
        id = Some(2L),
        title = "Test thread #2",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 11, 19).getTime
      )
    )

    def posts = Seq(
      Post(
        id = Some(1L),
        content = "This is a test thread post",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 10, 16).getTime,
        threadId = Some(1L)
      ),
      Post(
        id = Some(2L),
        content = "This is a test reply post",
        author = "(also) Jakob",
        createdDate = new GregorianCalendar(2017, 10, 19).getTime,
        threadId = Some(1L)
      ),
      Post(
        id = Some(3L),
        content = "This is another test thread post with <u>valid formatting</u> and <span>escaped formatting</span>",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 10, 16).getTime,
        threadId = Some(2L)
      )
    )
  }
}