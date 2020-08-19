package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.explode

object _01_FlattenShipmentDisplayApp extends App with LazyLogging {

  val spark = SparkSession
    .builder()
    .appName("Flattening JSON doc describing shipments")
    .master("local[*]")
    .getOrCreate()

  val df = spark.read.format("json").option("multiline", value = true).load("data/shipment.json")

  val df0 = df
    .withColumn("supplier_name", df.col("supplier.name"))
    .withColumn("supplier_city", df.col("supplier.city"))
    .withColumn("supplier_state", df.col("supplier.state"))
    .withColumn("supplier_country", df.col("supplier.country"))
    .drop("supplier")
    .withColumn("customer_name", df.col("customer.name"))
    .withColumn("customer_city", df.col("customer.city"))
    .withColumn("customer_state", df.col("customer.state"))
    .withColumn("customer_country", df.col("customer.country"))
    .drop("customer")
    .withColumn("items", explode(df.col("books")))

  val df1 = df0
    .withColumn("qty", df0.col("items.qty"))
    .withColumn("title", df0.col("items.title"))
    .drop("items")
    .drop("books")

  df1.show(5, truncate = false)
  df1.printSchema()

  df1.createOrReplaceTempView("shipment_detail")
  val bookCountDf = spark.sql("select count(*) as titleCount from shipment_detail")
  bookCountDf.show(truncate = false)
}
