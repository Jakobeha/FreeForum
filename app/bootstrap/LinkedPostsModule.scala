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
        title = "Wikipedia",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 11, 28).getTime
      ),
      Thread(
        id = Some(2L),
        title = "PageRank",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 11, 28).getTime
      ),
      Thread(
        id = Some(3L),
        title = "Volkswagen",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 11, 28).getTime
      ),
      Thread(
        id = Some(4L),
        title = "Bibliography",
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 11, 28).getTime
      )
    )

    def posts = Seq(
      Post(
        id = Some(1L),
        content =
          """
            |If you ever needed to find information about something, you've probably used
            |<a href="https://www.wikipedia.org">Wikipedia</a>, "the Free Encyclopedia".
            |You probably already know that Wikipedia is a website which contains information about almost anything.
            |The information on Wikipedia is generally easier to understand than on other sites, although it's not always accurate.
            |<p>
            |However, you might not realize how much Wikipedia relies on support.
            |Wikipedia is
            |<a href="https://en.wikipedia.org/wiki/Wikipedia:About">completely powered</a>
            |by the support and contributions of regular people.
            |All of its entries are created, edited, and verified by users.
            |If you don't see an entry for something, you can even create one yourself.
            |Moreover, Wikipedia is
            |<a href="https://donate.wikimedia.org/w/index.php?title=Special:LandingPage&country=US&utm_medium=sidebar&utm_source=donate&utm_campaign=C13_wikimediafoundation.org">completely funded</a>
            |by donations from users -- it's completely free, and doesn't even have any advertisements.
            |A lot of people know that Wikipedia relies on its users' contributions.
            |But a lot of people overlook this, and don't contribute themselves.
            |Millions of people have probably used Wikipedia in the last 30 days, but much less actually contribute -- as of 7:36 on November 29, only 136,573 have actually edited a page (“About”).
            |You can -- and should -- actually improve Wikipedia yourself.
            |And you can do so by fixing existing entries, creating a new entry, or donating.
            |</p>
          """.stripMargin,
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 10, 28).getTime,
        threadId = Some(1L)
      ),
      Post(
        id = Some(2L),
        content =
          """
            |You might not have heard of PageRank before.
            |But you've probably used Google search, and PageRank is what ultimately powers Google's search engine.
            |<p>
            |PageRank is
            |<a href="http://www.math.cornell.edu/~mec/Winter2009/RalucaRemus/Lecture3/lecture3.html">an algorithm</a>
            |developed by Larry Page and Sergey Brin, who are also the founders of Google.
            |Essentially, it takes a set of web-pages, and assigns them rankings.
            |A page gets a higher ranking if other pages contain links going to that page, especially if those other pages have high rankings themselves.
            |When you run a Google search, Google shows you the web-pages with the highest rankings first.
            |And these are usually popular, useful web-pages -- exactly the ones you usually want -- because they're referenced a lot (Page, Larry).
            |</p>
            |<p>
            |PageRank is also
            |<a href="https://web.archive.org/web/20020506051802/http://www-diglib.stanford.edu/cgi-bin/WP/get/SIDL-WP-1997-0072?1">a research project</a>,
            |which Larry Page and Sergey Brin created when they were at Stanford.
            |Furthermore, they based it off of
            |<a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.728.4415&rep=rep1&type=pdf">another research paper</a>
            |by two other scientists, Gabriel Pinski and Francis Narin.
            |Pinski and Narin had a similar algorithm, but instead of web-pages and links, they designed their algorithm for scholarly articles and citations --
            |an article gets a higher ranking if other articles cite it (Pinski and Narin 297-312).
            |If Page and Brin, or even Pinski and Narin, didn't stay motivated while working on their research,
            |then we wouldn't have PageRank, and Google search would almost definitely be worse today.
            |That's why it's important to support and motivate researchers.
            |</p>
          """.stripMargin,
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 10, 28).getTime,
        threadId = Some(2L)
      ),
      Post(
        id = Some(3L),
        content =
          """
            |You've probably heard about the scandal involving Volkswagen.
            |Old Volkswagen cars emit a lot of of pollution (NO<sup>2</sup>) into the atmosphere -- sometimes 35 times as much as they're legally allowed to.
            |Because of this, these cars should've been banned from being sold and driven on the road.
            |But they were programmed to emit much less pollution than usual when they were tested in a laboratory.
            |So they passed emissions tests and weren't banned.
            |Around 2 years ago, people tested the Volkswagen cars' emissions on the road, found that they were much higher than in the lab, and discovered this scandal.
            |Since then, the cars have been taken off the road, and Volkswagen has been penalized, to prevent them from doing this again (Lam).
            |<p>
            |The people who discovered the scandal are researchers, who discovered it during their research project.
            |They actually started this project after they got money from a grant -- in other words, they started it because they got support.
            |Supporting existing research projects isn't the same as supporting new ones -- replying to a post probably won't motivate someone as much as giving them a grant.
            |But both methods are useful, because they provide some motivation to researchers to continue their research, and make important discoveries like this scandal.
            |It's very good that this scandal was discovered, or else these cars would still be sold and driven, so they would still cause a lot of pollution.
            |Because the researchers got support and stayed motivated, they were able to make an important discovery --
            |a discovery which has stopped this scandal, and in doing so, has ended up reducing pollution and enforcing justice (Lam).
            |</p>
          """.stripMargin,
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 10, 28).getTime,
        threadId = Some(3L)
      ),
      Post(
        id = Some(4L),
        content =
          """
            |Kamp, Poul-Henning. “Quality Software Costs Money---Heartbleed Was Free.” Communications of the ACM, vol. 57, no. 8, 2014, pp. 49–51.<br />
            |Gambier, Jimmy. “An Independent Social Game Company's Perspective.” Computer, vol. 45, no. 2, 2012, pp. 85–87.<br />
            |“Software Free for the Asking, Or for a Nominal &Apos;Donation.&Apos;” Computerworld, vol. 18, no. 39, 1984, p. 6.<br />
            |Page, Larry. “Working Paper SIDL-WP-1997-0072.” <i>Wayback Machine</i>, Stanford Digital Library Project, 15 Sept. 1997, web.archive.org/web/20020506051802/http://www-diglib.stanford.edu/cgi-bin/WP/get/SIDL-WP-1997-0072?1.<br />
            |Pinski, and Narin. “Citation Influence for Journal Aggregates of Scientific Publications: Theory, with Application to the Literature of Physics.” Information Processing and Management, vol. 12, no. 5, 1976, pp. 297–312.<br />
            |“About” Wikipedia, Wikimedia Foundation, 29 Nov. 2017, en.wikipedia.org/wiki/Wikipedia:About.<br />
            |Apple; AppleInsider. “Number of available apps 2017.” Statista, Jan. 2017, www.statista.com/statistics/263795/number-of-available-apps-in-the-apple-app-store/.<br />
            |“Total number of Websites.” Internet Live Stats, 2 Nov. 2017, www.internetlivestats.com/total-number-of-websites/.<br />
            |Buckley, Louise. “Academic Study, Wallpapering Chicken Pens and a ’Roach in Your Knicker Draw: Life as a PhD Student.” Veterinary Nursing Journal, vol. 32, no. 2, 2017, pp. 51–53.<br />
            |Lam, Bourree. “The Academic Paper That Broke the Volkswagen Scandal.” <i>The Atlantic</i>, Atlantic Media Company, 25 Sept. 2015, www.theatlantic.com/business/archive/2015/09/volkswagen-scandal-cheating-emission-virginia-epa/407425/.<br />
            |“Play Framework.” <i>Play Framework</i>, 2.6.7, Zengularity & Lightbend, www.playframework.com.<br />
            |“Slick.” <i>Slick</i>, 3.2.1, Lightbend, slick.lightbend.com.<br />
            |“Scalingo.” <i>Scalingo</i>, Scalingo, scalingo.com/
          """.stripMargin,
        author = "Jakob",
        createdDate = new GregorianCalendar(2017, 10, 28).getTime,
        threadId = Some(4L)
      )
    )
  }
}