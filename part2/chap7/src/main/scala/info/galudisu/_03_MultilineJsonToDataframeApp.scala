package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _03_MultilineJsonToDataframeApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Multiline JSON to Dataframe").master("local[*]").getOrCreate()

  val df = spark.read.format("json").option("multiline", "true").load("data/countrytravelinfo.json")

  df.show(3)
  df.printSchema()

  spark.stop()
}
