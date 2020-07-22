package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  * JSON 格式文件的处理
  */
object _02_JsonIngestionSchemaManipulationApp extends App with LazyLogging {
  // Creates a Spark session
  val spark = SparkSession
    .builder()
    .appName("Restaurants in Durham County, NC")
    .master("local")
    .getOrCreate()

  // Creates a dataframe (a Dataset<Row>)
  var df = spark.read.format("json").load("data/Restaurants_in_Durham_County_NC.json")
  logger.debug("*** Right after ingestion")

  df.show(5)
  df.printSchema()

  // 要访问字段结构，你可以使用dot(.)符号；要访问数组中的元素，你可以使用`getItem()`方法：
  df = df
    .withColumn("county", lit("Durham"))
    // 使用下标访问json的字段
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
    // 描述部分是个<id> - <label>格式，对字符串进行分割
    .withColumn("type", split(df.col("fields.type_description"), " - ").getItem(1))
    // 提取地理位置信息
    .withColumn("geoX", df.col("fields.geolocation").getItem(0))
    .withColumn("geoY", df.col("fields.geolocation").getItem(1))

  // 添加unique identified
  df = df.withColumn("id", concat(df.col("state"), lit("_"), df.col("county"), lit("_"), df.col("datasetId")))
  logger.debug("*** Dataframe transformed")
  df.show(5)
  df.printSchema()
}
