package myutils

import java.text.SimpleDateFormat
import java.util.Date
import java.time.Instant

object DateUtils {
  val format = new SimpleDateFormat("hh:mm aa, dd/MM/yyyy zzz")
  val forumUnlockDate: Date = format.parse("10:45 AM, 07/12/2017 EST")
  val forumLockDate: Date = format.parse("11:45 AM, 07/12/2017 EST")

  def now(): Date = Date.from(Instant.now())
}
