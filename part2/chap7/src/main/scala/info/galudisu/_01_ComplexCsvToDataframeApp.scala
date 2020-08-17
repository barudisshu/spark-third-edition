package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

/**
  * 复杂数据类型
  */
object _01_ComplexCsvToDataframeApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Complex CSV to Dataframe").master("local[*]").getOrCreate()

  val df =
    spark.read
      .format("csv")
      .option("header", "true")
      .option("multiline", value = true)
      .option("sep", ";")
      .option("quote", "*")
      .option("dateFormat", "MM/dd/yyyy")
      .option("inferSchema", value = true)
      .load("data/books2.csv")

  logger.debug("*** Excerpt of the dataframe content: ")
  df.show(7, 70)

  logger.debug("*** Dataframe's schema: ")
  df.printSchema()

  spark.stop()
}
