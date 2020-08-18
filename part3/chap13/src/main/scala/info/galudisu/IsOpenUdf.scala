package info.galudisu

import java.sql.Timestamp

import org.apache.spark.sql.api.java.UDF8

@SerialVersionUID(-216751L)
case class IsOpenUdf() extends UDF8[String, String, String, String, String, String, String, Timestamp, Boolean] {
  @throws[Exception]
  override def call(hoursMon: String,
                    hoursTue: String,
                    hoursWed: String,
                    hoursThu: String,
                    hoursFri: String,
                    hoursSat: String,
                    hoursSun: String,
                    dateTime: Timestamp): Boolean = {

    IsOpenService.isOpen(
      hoursMon,
      hoursTue,
      hoursWed,
      hoursThu,
      hoursFri,
      hoursSat,
      hoursSun,
      dateTime
    )
  }
}
