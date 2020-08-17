package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _04_XmlToDataframeApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("XML to Dataframe").master("local[*]").getOrCreate()

  val df = spark.read.format("xml").option("rowTag", "row").load("data/nasa-patents.xml")

  df.show(5)
  df.printSchema()

}
