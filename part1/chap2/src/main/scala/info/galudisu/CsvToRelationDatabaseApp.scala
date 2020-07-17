package info.galudisu

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SaveMode, SparkSession}

object CsvToRelationDatabaseApp extends App with LazyLogging {

  // Create a session on a local master
  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Sample App")
    .getOrCreate()

  // Read a CSV file with header, called authors.csv, and store it in a dataframe
  var df = spark.read.format("csv").option("header", "true").load("data/authors.csv")

  // Create a new column called "name" as the concatenation of lname, a virtual column containing ", " and the fname column
  df = df.withColumn("name", concat(df.col("lname"), lit(", "), df.col("fname")))

  val dbConnectionUrl = "jdbc:mysql://localhost/spark_labs"
  val prop            = new Properties()
  prop.setProperty("driver", "com.mysql.cj.jdbc.Driver")
  prop.setProperty("user", "root")
  prop.setProperty("password", "root")

  // Write dataframe to RDBMS
  df.write.mode(SaveMode.Overwrite).jdbc(dbConnectionUrl, "chap02", prop)

  logger.debug("Process complete")
}
