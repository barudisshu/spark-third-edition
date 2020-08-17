package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

import scala.util.{Failure, Success, Try}

/**
  * socket 流
  */
object _02_ReadLinesFromNetworkStreamApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Read Lines from Network").master("local[*]").getOrCreate()

  val df = spark.readStream.format("socket").option("host", "localhost").option("port", 9999).load()

  val query = df.writeStream.outputMode(OutputMode.Append()).format("console").start()

  Try(query.awaitTermination(60000)) match {
    case Failure(exception) =>
      logger.error("Exception while waiting for query to end {}", exception.getMessage, exception)
    case Success(_) =>
  }

  logger.debug("*** Query status: {}", query.status)

  spark.stop()
}
