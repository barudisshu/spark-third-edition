package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _08_ParquetToDataframeApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Parquet to Dataframe").master("local[*]").getOrCreate()

  val df = spark.read.format("parquet").load("data/alltypes_plain.parquet")

  df.show(10)
  df.printSchema()

  logger.debug("*** The dataframe has {} rows.", df.count())

  spark.stop()
}
