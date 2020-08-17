package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _01_PhotoMetadataIngestionApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("EXIF to Dataset").master("local[*]").getOrCreate()

  val df = spark.read
    .format("image")
    .option("dropInvalid", value = true)
    .load("images")

  logger.debug("*** I have imported {} photos.", df.count())
  df.printSchema()
  df.select("image.origin", "image.width", "image.height").show(truncate = false)

  spark.stop()
}
