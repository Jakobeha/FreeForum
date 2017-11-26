package myutils

import java.util.Date
import java.time.{Instant}

object DateUtils {
  def now(): Date = Date.from(Instant.now())
}
