package info.galudisu

import java.sql.Timestamp
import java.util.Calendar

object IsOpenService {
  def isOpen(hoursMon: String,
             hoursTue: String,
             hoursWed: String,
             hoursThu: String,
             hoursFri: String,
             hoursSat: String,
             hoursSun: String,
             dateTime: Timestamp): Boolean = {

    val cal = Calendar.getInstance()
    cal.setTimeInMillis(dateTime.getTime)

    val day = cal.get(Calendar.DAY_OF_WEEK)
    val hours = day match {
      case Calendar.MONDAY    => hoursMon
      case Calendar.TUESDAY   => hoursTue
      case Calendar.WEDNESDAY => hoursWed
      case Calendar.THURSDAY  => hoursThu
      case Calendar.FRIDAY    => hoursFri
      case Calendar.SATURDAY  => hoursSat
      case Calendar.SUNDAY    => hoursSun
    }

    if (hours.compareToIgnoreCase("closed") == 0) {
      return false
    }

    val event = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)

    val ranges = hours.split(" and ")

    for (i <- Range(0, ranges.length)) {
      val openingHours = ranges(i).split("-")
      val start = Integer.valueOf(openingHours(0).substring(0, 2)) * 3600 + Integer.valueOf(
        openingHours(0).substring(3, 5)) * 60
      val end = Integer.valueOf(openingHours(1).substring(0, 2)) * 3600 + Integer.valueOf(
        openingHours(1).substring(3, 5)) * 60

      if (event >= start && event <= end) {
        return true
      }
    }
    false
  }
}
