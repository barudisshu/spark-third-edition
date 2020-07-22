package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object _01_IngestionSchemaManipulationApp extends App with LazyLogging {

  // Creates a Spark session
  val spark = SparkSession
    .builder()
    .appName("Restaurants in Wake County, NC")
    .master("local")
    .getOrCreate()

  // Creates a dataframe (a Dataset<Row>)
  var df = spark.read.format("csv").option("header", "true").load("data/Restaurants_in_Wake_County_NC.csv")
  logger.debug("*** Right after ingestion")

  // Shows five records/rows
  df.show(5)

  // 打印 dataframe的Schema
  df.printSchema()
  logger.debug(s"*** We have ${df.count()} records.")

  // 每行新增一个列`country`，值为 `Wake`
  df = df
    .withColumn("county", lit("Wake"))
    // 重命名列
    .withColumnRenamed("HSISID", "datasetId")
    .withColumnRenamed("NAME", "name")
    .withColumnRenamed("ADDRESS1", "address1")
    .withColumnRenamed("ADDRESS2", "address2")
    .withColumnRenamed("CITY", "city")
    .withColumnRenamed("STATE", "state")
    .withColumnRenamed("POSTALCODE", "zip")
    .withColumnRenamed("PHONENUMBER", "tel")
    .withColumnRenamed("RESTAURANTOPENDATE", "dateStart")
    .withColumnRenamed("FACILITYTYPE", "type")
    .withColumnRenamed("X", "geoX")
    .withColumnRenamed("Y", "geoY")
    // dropped某些列
    .drop("OBJECTID")
    .drop("PERMITID")
    .drop("GEOCODESTATUS")

  // 添加一个Unique identifier，它由state_county_datasetId 格式拼接而成
  df = df.withColumn("id", concat(df.col("state"), lit("_"), df.col("county"), lit("_"), df.col("datasetId")))

  logger.debug("*** Dataframe transformed")
  df.show(5)
  df.printSchema()

  // 数据被存储在分区中，目前仅有一个
  logger.debug("*** Looking at partitions")
  val partitions     = df.rdd.partitions
  val partitionCount = partitions.length
  logger.debug(s"*** Partition count before repartition: $partitionCount")

  // 分区扩展为4个
  df = df.repartition(4)
  logger.debug(s"*** Partition count after repartition: ${df.rdd.partitions.length}")

  // 深挖Schema的内容
  val schema = df.schema
  logger.debug("*** Schema as a tree:")
  schema.printTreeString()

  val schemaAsString = schema.mkString
  logger.debug(s"*** Schema as string: $schemaAsString")

  val schemaAsJson = schema.prettyJson
  logger.debug(s"*** Schema as JSON: $schemaAsJson")
}
