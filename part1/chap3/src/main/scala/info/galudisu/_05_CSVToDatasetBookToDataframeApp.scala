package info.galudisu

import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

import scala.util._

trait _05_CSVToDatasetBookToDataframeApp {
  case class Book(id: Int, authorId: Int, link: String, title: String, releaseDate: LocalDate)
}

/**
  * 在 dataset 和 dataframe 之间互转
  */
object _05_CSVToDatasetBookToDataframeApp extends App with _05_CSVToDatasetBookToDataframeApp with LazyLogging {

  val spark = SparkSession
    .builder()
    .appName("CSV to dataframe to Dataset<Book> and back")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  val df = spark.read.format("csv").option("inferSchema", "true").option("header", "true").load("data/books.csv")
  logger.debug("*** Books ingested in a dataframe")
  df.show(5)
  df.printSchema()

  val formatter = new DateTimeFormatterBuilder()
    .appendPattern("M/d/")
    .appendValueReduced(ChronoField.YEAR, 2, 4, 1900) // 年份从1900年开始。采取ISO 8601 记录方式
    .toFormatter()

  val parseToDate: String => Option[LocalDate] = k =>
    Try(LocalDate.parse(k, formatter)) match {
      case Failure(_)     => None
      case Success(value) => Some(value)
  }

  val parseToBook: Row => Option[Book] =
    row => {
      parseToDate(row.getAs[String]("releaseDate")).map(date => {
        Book(
          row.getAs[Int]("id"),
          row.getAs[Int]("authorId"),
          row.getAs[String]("link"),
          row.getAs[String]("title"),
          date
        )
      })
    }

  val bookDs: Dataset[Book] = df.flatMap(parseToBook(_))

  logger.debug("*** Books are now in a dataset of books")
  bookDs.show(5, 17)
  bookDs.printSchema()

  // DataSet[T] 转换为 DataFrame
  var df2 = bookDs.toDF()

  // 从spark 3.0 开始内置提供对日期的支持。
  //
  df2 = df2.withColumn(
    "releaseDateAsString",
    concat(year($"releaseDate").plus(104), // 补充并更正新历年
           lit("-"),
           date_format(add_months($"releaseDate", 1), "MM"),
           lit("-"),
           date_format($"releaseDate", "dd"))
  )

  df2 = df2
    .withColumn("releaseDateAsDate", to_date($"releaseDateAsString", "yyyy-MM-dd"))
    .drop("releaseDateAsString")

  logger.debug("*** Books are back in a dataframe")
  df2.show(5)
  df2.printSchema()
}
