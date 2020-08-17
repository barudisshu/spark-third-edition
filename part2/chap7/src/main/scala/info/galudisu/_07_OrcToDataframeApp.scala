package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _07_OrcToDataframeApp extends App with LazyLogging {

  val spark = SparkSession
    .builder()
    .appName("ORC to Dataframe")
    .config("spark.sql.orc.impl", "native")
    .master("local[*]")
    .getOrCreate()

  val df = spark.read.format("orc").load("data/demo-11-zlib.orc")

  df.show(10)
  df.printSchema()

  logger.debug("*** The dataframe has {} rows.", df.count())

  spark.stop()

}
