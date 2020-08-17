package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _05_TextToDataframeApp extends App with LazyLogging {
  val spark = SparkSession.builder().appName("Text to Dataframe").master("local[*]").getOrCreate()

  val df = spark.read.format("text").load("data/romeo-juliet-pg1777.txt")

  df.show(10)
  df.printSchema()

  spark.stop()
}
