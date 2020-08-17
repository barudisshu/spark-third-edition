package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

import scala.util.{Failure, Success, Try}

/**
  * Spark 允许以流的方式读取数据源
  */
object _01_ReadLinesFromFileStreamApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Read lines over a file stream").master("local").getOrCreate()

  val df = spark.readStream.format("text").load("texts")

  val query = df.writeStream
    .outputMode(OutputMode.Append())
    .format("console")
    .option("truncate", value = false)
    .option("numRows", 20)
    .start()

  Try(query.awaitTermination(60000)) match {
    case Failure(exception) =>
      logger.error("Exception while waiting for query to end {}.", exception.getMessage, exception)
    case Success(_) =>
  }

  spark.stop()

}
