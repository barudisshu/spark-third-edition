package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object _01_OrderStatisticsApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Orders analytics").master("local[*]").getOrCreate()

  val df =
    spark.read.format("csv").option("header", value = true).option("inferSchema", value = true).load("data/orders.csv")

  val apiDf = df
    .groupBy(col("firstName"), col("lastName"), col("state"))
    .agg(
      sum("quantity"),
      sum("revenue"),
      avg("revenue")
    )

  apiDf.show(20)

  // 等效的Spark SQL
  df.createOrReplaceTempView("orders")

  val sqlDf = spark.sql(
    "select firstName, lastName, state, sum(quantity), sum(revenue), avg(revenue) from orders group by firstName, lastName, state")
  sqlDf.show(20)

  spark.stop()
}
