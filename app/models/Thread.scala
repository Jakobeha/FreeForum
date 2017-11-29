package models

import java.util.Date
import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.{ExecutionContext, Future}

case class Thread(id: Option[Long],
                  title: String,
                  author: String,
                  createdDate: Date) {
  def recency: Option[Long] = id
}

trait ThreadComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  protected class ThreadTable(tag: Tag) extends Table[Thread](tag, "Thread") {
    implicit private def dateType: BaseColumnType[Date] =
      MappedColumnType.base[Date, Long](_.getTime, new Date(_))

    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("Title")
    def author = column[String]("Author")
    def createdDate = column[Date]("CreatedDate")

    def * = (id.?, title, author, createdDate) <> (Thread.tupled, Thread.unapply)
  }

  protected val threads = TableQuery[ThreadTable]

  protected lazy val threadsById = threads.findBy(_.id)
}

class ThreadDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                         (implicit executionContext: ExecutionContext)
  extends ThreadComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  def lazyLoad(initThreads: => Seq[Thread]): Future[Boolean] = for {
    tables <- db.run(MTable.getTables)
    tableNames = tables.map(_.name.name)
    result <- if (tableNames.contains("Post")) {
      Future { false }
    } else {
      db.run(DBIO.seq(
        threads.schema.create,
        threads ++= initThreads
      )).map { _ => true }
    }
  } yield result

  def all: Future[Seq[Thread]] = db.run(threads.result)

  def withId(id: Long): Future[Option[Thread]] = db.run(threadsById(id).result).map(_.headOption)

  def insert(thread: Thread): Future[Thread] =
    db.run((threads returning threads.map(_.id)) += thread).map { threadId =>
    thread.copy(id = Some(threadId))
  }
}