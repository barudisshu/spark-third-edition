package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object _02_PointsPerOrder extends App with LazyLogging{

  val spark = SparkSession.builder().appName("Orders loyalty point").master("local[*]").getOrCreate()

  spark.udf.register("pointAttribution", PointAttributionUdaf())

  val df = spark.read.format("csv").option("header", value = true).option("inferSchema", value = true).load("data/orders.csv")

  val pointDf = df.groupBy(col("firstName"), col("lastName"), col("state")).agg(
    sum("quantity"),
    callUDF("pointAttribution", col("quantity")).as("point")
  )

  pointDf.show(20)

  spark.stop()

}
