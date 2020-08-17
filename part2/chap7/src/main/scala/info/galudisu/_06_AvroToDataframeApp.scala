package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

/**
  * 大数据文件Avro
  */
object _06_AvroToDataframeApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Avro to Dataframe").master("local[*]").getOrCreate()

  val df = spark.read.format("avro").load("data/weather.avro")

  df.show(10)
  df.printSchema()

  logger.debug("*** The dataframe has {} rows.", df.count())

  spark.stop()
}
