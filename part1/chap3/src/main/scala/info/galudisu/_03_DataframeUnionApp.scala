package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  * 组合两个dataframe
  *
  * 我们希望将 Wake区餐厅的数据，和 Durham区的餐厅数据进行组合
  */
object _03_DataframeUnionApp extends App with LazyLogging {

  // Creates a Spark session
  val spark = SparkSession
    .builder()
    .appName("Union of two dataframes")
    .master("local")
    .getOrCreate()

  val wakeRestaurantsDf   = buildWakeRestaurantsDataframe()
  val durhamRestaurantsDf = buildDurhamRestaurantsDataframe()

  val unionDf = combineDataframes(wakeRestaurantsDf, durhamRestaurantsDf)

  val schema = unionDf.schema

  logger.debug(s"*** The union schema ${schema.prettyJson}")

  def combineDataframes(df1: Dataset[Row], df2: Dataset[Row]): Dataset[Row] = {
    // 根据名字进行组合
    val df = df1.unionByName(df2)
    df.show(5)
    df.printSchema()

    logger.debug(s"*** We have ${df.count()} records.")

    val partitions     = df.rdd.partitions
    val partitionCount = partitions.length

    logger.debug(s"*** Partition count: $partitionCount")

    df
  }

  def buildWakeRestaurantsDataframe(): Dataset[Row] = {
    var df = spark.read.format("csv").option("header", "true").load("data/Restaurants_in_Wake_County_NC.csv")

    df = df
      .withColumn("county", lit("Wake"))
      .withColumnRenamed("HSISID", "datasetId")
      .withColumnRenamed("NAME", "name")
      .withColumnRenamed("ADDRESS1", "address1")
      .withColumnRenamed("ADDRESS2", "address2")
      .withColumnRenamed("CITY", "city")
      .withColumnRenamed("STATE", "state")
      .withColumnRenamed("POSTALCODE", "zip")
      .withColumnRenamed("PHONENUMBER", "tel")
      .withColumnRenamed("RESTAURANTOPENDATE", "dateStart")
      .withColumn("dateEnd", lit(null))
      .withColumnRenamed("FACILITYTYPE", "type")
      .withColumnRenamed("X", "geoX")
      .withColumnRenamed("Y", "geoY")
      .drop("OBJECTID")
      .drop("PERMITID")
      .drop("GEOCODESTATUS")

    df.withColumn("id", concat(df.col("state"), lit("_"), df.col("county"), lit("_"), df.col("datasetId")))
  }

  def buildDurhamRestaurantsDataframe(): Dataset[Row] = {
    var df = spark.read.format("json").load("data/Restaurants_in_Durham_County_NC.json")
    df = df
      .withColumn("county", lit("Durham"))
      .withColumn("datasetId", df.col("fields.id"))
      .withColumn("name", df.col("fields.premise_name"))
      .withColumn("address1", df.col("fields.premise_address1"))
      .withColumn("address2", df.col("fields.premise_address2"))
      .withColumn("city", df.col("fields.premise_city"))
      .withColumn("state", df.col("fields.premise_zip"))
      .withColumn("zip", df.col("fields.premise_zip"))
      .withColumn("tel", df.col("fields.premise_phone"))
      .withColumn("dateStart", df.col("fields.opening_date"))
      .withColumn("dateEnd", df.col("fields.closing_date"))
      .withColumn("type", split(df.col("fields.type_description"), " - ").getItem(1))
      .withColumn("geoX", df.col("fields.geolocation").getItem(0))
      .withColumn("geoY", df.col("fields.geolocation").getItem(1))
      .drop(df.col("fields"))
      .drop(df.col("geometry"))
      .drop(df.col("record_timestamp"))
      .drop(df.col("recordid"))

    df.withColumn("id", concat(df.col("state"), lit("_"), df.col("county"), lit("_"), df.col("datasetId")))
  }
}
