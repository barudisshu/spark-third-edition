package info.galudisu

import org.apache.spark.sql.SparkSession

object FirstSparkApplication extends App {

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Sample App")
    .getOrCreate()

  val df = spark.read.format("csv").option("header", "true").load("data/books.csv")
  df.show(5)
}
