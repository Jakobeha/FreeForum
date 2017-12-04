package myutils

import java.util.{Date, GregorianCalendar}
import java.time.Instant

object DateUtils {
  val forumUnlockDate: Date = new GregorianCalendar(2017, 12, 4, 11, 30, 0).getTime
  val forumLockDate: Date = new GregorianCalendar(2017, 12, 4, 13, 35, 0).getTime

  def now(): Date = Date.from(Instant.now())
}
