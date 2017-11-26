package models

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.{Inject, Singleton}
import java.util.Date

import myutils.HtmlUtils
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.twirl.api.Html
import slick.jdbc.meta.MTable

case class Post(id: Option[Long],
                content: String,
                author: String,
                createdDate: Date,
                threadId: Option[Long]) {
  lazy val htmlContent: Html = HtmlUtils.escapeUnsafe(content)
}


trait PostComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  protected class PostTable(tag: Tag) extends Table[Post](tag, "Post") {
    implicit private def dateType: BaseColumnType[Date] =
      MappedColumnType.base[Date, Long](_.getTime, new Date(_))

    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def content = column[String]("Content")
    def author = column[String]("Author")
    def createdDate = column[Date]("CreatedDate")
    def threadId = column[Option[Long]]("ThreadId")

    def * = (id.?, content, author, createdDate, threadId) <> (Post.tupled, Post.unapply)
  }

  protected val posts = TableQuery[PostTable]
}

@Singleton()
class PostDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext)
  extends PostComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  def lazyLoad(initPosts: => Seq[Post]): Future[Boolean] = for {
    tables <- db.run(MTable.getTables)
    tableNames = tables.map(_.name.name)
    result <- if (tableNames.contains("Post")) {
      Future { false }
    } else {
      db.run(DBIO.seq(
        posts.schema.create,
        posts ++= initPosts
      )).map { _ => true }
    }
  } yield result

  def all: Future[Seq[Post]] = db.run(posts.result)

  def insert(post: Post): Future[Post] =
    db.run((posts returning posts.map(_.id)) += post).map { postId =>
    post.copy(id = Some(postId))
  }
}