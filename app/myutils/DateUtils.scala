package myutils

import java.text.SimpleDateFormat
import java.util.Date
import java.time.Instant

object DateUtils {
  val format = new SimpleDateFormat("hh:mm aa, dd/MM/yyyy")
  val forumUnlockDate: Date = format.parse("1:33 PM, 04/12/2017")
  val forumLockDate: Date = format.parse("1:37 PM, 04/12/2017")

  def now(): Date = Date.from(Instant.now())
}
