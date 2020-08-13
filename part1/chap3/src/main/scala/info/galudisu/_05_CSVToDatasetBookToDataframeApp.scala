package info.galudisu

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  * 在 dataset 和 dataframe 之间互转
  */
object _05_CSVToDatasetBookToDataframeApp extends App with LazyLogging {
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

  case class Book(id: Int, authorId: Int, link: String, title: String, releaseDate: Option[java.sql.Timestamp])

  val parser = new SimpleDateFormat("M/d/yy")

  val bookDs = df.map(row => {
    Book(
      row.getAs[Int]("id"),
      row.getAs[Int]("authorId"),
      row.getAs[String]("link"),
      row.getAs[String]("title"),
      Option(row.getAs[String]("releaseDate")).map((r: String) => {
        new Timestamp(parser.parse(r).getTime)
      })
    )
  })

  logger.debug("*** Books are now in a dataset of books")
  bookDs.show(5, 17)
  bookDs.printSchema()

  var df2 = bookDs.toDF()

  df2 = df2.withColumn(
    "releaseDateAsString",
    concat(expr("releaseDate.year + 1900"),
           lit("-"),
           expr("releaseDate.month + 1"),
           lit("-"),
           df2.col("releaseDate.date"))
  )

  df2 = df2
    .withColumn("releaseDateAsDate", to_date(df2.col("releaseDateAsString"), "yyyy-MM-dd"))
    .drop("releaseDateAsString")

  logger.debug("*** Books are back in a dataframe")
  df2.show(5)
  df2.printSchema()
}
