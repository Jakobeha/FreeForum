package myutils

import java.text.SimpleDateFormat
import java.util.Date
import java.time.Instant

object DateUtils {
  val format = new SimpleDateFormat("hh:mm aa, dd/MM/yyyy zzz")
  val forumUnlockDate: Date = format.parse("1:45 PM, 04/12/2017 EST")
  val forumLockDate: Date = format.parse("2:00 PM, 04/12/2017 EST")

  def now(): Date = Date.from(Instant.now())
}
