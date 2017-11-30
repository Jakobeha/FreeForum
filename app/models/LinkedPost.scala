package models

import java.util.Date

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.{Inject, Singleton}

import myutils.DateUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

/** A @Post@ with a reference to the @Thread@ it belongs to. */
case class LinkedPost(post: Post,
                      thread: Thread) {

}

trait LinkedPostComponent extends ThreadComponent with PostComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  protected lazy val linkedPosts = for {
    (post, thread) <- posts join threads on { (post, thread) =>
      post.threadId === thread.id
    }
  } yield (post, thread)

  protected lazy val threadPosts = {
    implicit def dateType: BaseColumnType[Date] =
      MappedColumnType.base[Date, Long](_.getTime, new Date(_))

    for {
      (linkedPost, _) <- linkedPosts join postDates on {
        case (linkedPost, (threadId, postDate)) =>
          linkedPost._1.threadId === threadId && linkedPost._1.createdDate === postDate
      }
    } yield linkedPost
  }
}

@Singleton()
class LinkedPostDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                             (implicit executionContext: ExecutionContext)
  extends LinkedPostComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  def all: Future[Seq[LinkedPost]] = db.run(linkedPosts.result).map(_.map(LinkedPost.tupled))

  def allThreadPosts: Future[Seq[LinkedPost]] = db.run(threadPosts.result).map(_.map(LinkedPost.tupled))

  def count: Future[Int] = db.run(linkedPosts.length.result)

  def insertLinkedPost(linkedPost: LinkedPost): Future[LinkedPost] = {
    if (linkedPost.post.createdDate.after(DateUtils.forumUnlockDate) && linkedPost.post.createdDate.before(DateUtils.forumLockDate)) {
      db.run {
        val thread = linkedPost.thread
        ((threads returning threads.map(_.id)) += thread).flatMap { threadId =>
          val post = linkedPost.post.copy(threadId = Some(threadId))
          ((posts returning posts.map(_.id)) += post).map { postId =>
            (threadId, postId)
          }
        }.transactionally
      }.map {
        case (threadId, postId) => linkedPost.copy(
          thread = linkedPost.thread.copy(id = Some(threadId)),
          post = linkedPost.post.copy(id = Some(postId), threadId = Some(threadId))
        )
      }
    } else {
      Future { linkedPost }
    }
  }

  def insertLinkedPosts(linkedPosts: Seq[LinkedPost]): Future[Seq[LinkedPost]] = {
    db.run {
      val newThreads = linkedPosts.map(_.thread)
      ((threads returning threads.map(_.id)) ++= newThreads).flatMap { threadIds =>
        val newPosts = (linkedPosts zip threadIds).map {
          case (linkedPost, threadId) => linkedPost.post.copy(id = Some(threadId))
        }
        ((posts returning posts.map(_.id)) ++= newPosts).map { postIds =>
          threadIds zip postIds
        }
      }.transactionally
    }.map { newLinkedPostIds => (newLinkedPostIds zip linkedPosts).map {
      case ((threadId, postId), threadPost) => threadPost.copy(
        thread = threadPost.thread.copy(id = Some(threadId)),
        post = threadPost.post.copy(id = Some(postId), threadId = Some(threadId))
      )
    } }
  }
}