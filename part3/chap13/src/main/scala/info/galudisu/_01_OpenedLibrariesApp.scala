package info.galudisu

import java.sql.Timestamp
import java.util.Calendar

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, Row, RowFactory, SparkSession}
import org.apache.spark.sql.api.java.UDF8
import org.apache.spark.sql.functions.{callUDF, col, lit, to_timestamp}
import org.apache.spark.sql.types.{DataTypes, StructType}
import scala.collection.JavaConverters._

trait _01_OpenedLibrariesApp {

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
        val operningHours = ranges(i).split("-")
        val start = Integer.valueOf(operningHours(0).substring(0, 2)) * 3600 + Integer.valueOf(
          operningHours(0).substring(3, 5)) * 60
        val end = Integer.valueOf(operningHours(1).substring(0, 2)) * 3600 + Integer.valueOf(
          operningHours(1).substring(3, 5)) * 60

        if (event >= start && event <= end) {
          return true
        }
      }
      false
    }
  }

  // 需要继承UDF特质
  case class IsOpenUdf() extends UDF8[String, String, String, String, String, String, String, Timestamp, Boolean] with Serializable {
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

  def createDataframe(spark: SparkSession): Dataset[Row] = {
    val schema: StructType = DataTypes.createStructType(
      Array(DataTypes.createStructField("date_str", DataTypes.StringType, false))
    )

    val rows: Seq[Row] = Seq(RowFactory.create("2019-03-11 14:30:00"),
                             RowFactory.create("2019-04-27 16:00:00"),
                             RowFactory.create("2020-01-26 05:00:00"))

    spark.createDataFrame(rows.asJava, schema).withColumn("date", to_timestamp(col("date_str"))).drop("date_str")
  }
}

object _01_OpenedLibrariesApp extends App with _01_OpenedLibrariesApp with LazyLogging {

  val spark = SparkSession.builder().appName("Custom UDF to check if in range").master("local[*]").getOrCreate()

  spark.udf.register("isOpen", IsOpenUdf(), DataTypes.BooleanType)

  val librariesDf = spark.read
    .format("csv")
    .option("header", value = true)
    .option("inferSchema", value = true)
    .option("encoding", "cp1252")
    .load("data/sdlibraries.csv")
    .drop("Administrative_Authority")
    .drop("Address1")
    .drop("Address2")
    .drop("Town")
    .drop("Postcode")
    .drop("County")
    .drop("Phone")
    .drop("Email")
    .drop("Website")
    .drop("Image")
    .drop("WGS84_Latitude")
    .drop("WGS84_Longitude")

  librariesDf.show(false)
  librariesDf.printSchema()

  val dateTimeDf = createDataframe(spark)
  dateTimeDf.show(false)
  dateTimeDf.printSchema()

  val df = librariesDf.crossJoin(dateTimeDf)
  df.show(false)

  val finalDf = df
    .withColumn(
      "open",
      callUDF(
        "isOpen",
        col("Opening_Hours_Monday"),
        col("Opening_Hours_Tuesday"),
        col("Opening_Hours_Wednesday"),
        col("Opening_Hours_Thursday"),
        col("Opening_Hours_Friday"),
        col("Opening_Hours_Saturday"),
        lit("Closed"),
        col("date")
      )
    )
    .drop("Opening_Hours_Monday")
    .drop("Opening_Hours_Tuesday")
    .drop("Opening_Hours_Wednesday")
    .drop("Opening_Hours_Thursday")
    .drop("Opening_Hours_Friday")
    .drop("Opening_Hours_Saturday")

  finalDf.show()

  spark.stop()
}
