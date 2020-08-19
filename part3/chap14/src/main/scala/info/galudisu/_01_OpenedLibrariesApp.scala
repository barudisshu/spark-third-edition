package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.functions.{callUDF, col, lit, to_timestamp}
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{RowFactory, SparkSession}

object _01_OpenedLibrariesApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Custom UDF to check if in range").master("local").getOrCreate()

  spark.udf.register("isOpen", IsOpenUdf(), DataTypes.BooleanType)

  val librariesDf = spark.read
    .format("csv")
    .option("header", value = true)
    .option("inferSchema", value = true)
    .option("encoding", "cp1252")
    .load("data/sdlibraries.csv")
    .drop("Administrative_Authority")
    .drop("Address1")
    .drop("Address2")
    .drop("Town")
    .drop("Postcode")
    .drop("County")
    .drop("Phone")
    .drop("Email")
    .drop("Website")
    .drop("Image")
    .drop("WGS84_Latitude")
    .drop("WGS84_Longitude")

  librariesDf.show(false)
  librariesDf.printSchema()

  // create dataframe
  val schema: StructType = DataTypes.createStructType(
    Array[StructField](DataTypes.createStructField("date_str", DataTypes.StringType, false))
  )

  val rows = Seq(RowFactory.create("2019-03-11 14:30:00"),
                 RowFactory.create("2019-04-27 16:00:00"),
                 RowFactory.create("2020-01-26 05:00:00"))

  val dateTimeDf = spark
    .createDataFrame(spark.sparkContext.parallelize(rows), schema)
    .withColumn("date", to_timestamp(col("date_str")))
    .drop("date_str")

  dateTimeDf.show(false)
  dateTimeDf.printSchema()

  val df = librariesDf.crossJoin(dateTimeDf)
  df.createOrReplaceTempView("libraries")
  df.show(false)

  val finalDf = df
    .withColumn(
      "open",
      callUDF(
        "isOpen",
        col("Opening_Hours_Monday"),
        col("Opening_Hours_Tuesday"),
        col("Opening_Hours_Wednesday"),
        col("Opening_Hours_Thursday"),
        col("Opening_Hours_Friday"),
        col("Opening_Hours_Saturday"),
        lit("Closed"),
        col("date")
      )
    )
    .drop("Opening_Hours_Monday")
    .drop("Opening_Hours_Tuesday")
    .drop("Opening_Hours_Wednesday")
    .drop("Opening_Hours_Thursday")
    .drop("Opening_Hours_Friday")
    .drop("Opening_Hours_Saturday")

  finalDf.show()

  spark.stop()

}
