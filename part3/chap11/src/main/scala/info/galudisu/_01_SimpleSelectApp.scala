package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes

object _01_SimpleSelectApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Simple SELECT using SQL").master("local[*]").getOrCreate()

  val schema = DataTypes.createStructType(
    Array(
      DataTypes.createStructField("geo", DataTypes.StringType, true),
      DataTypes.createStructField("yr1980", DataTypes.DoubleType, false)
    )
  )

  val df = spark.read
    .format("csv")
    .option("header", value = true)
    .schema(schema)
    .load("data/populationbycountry19802010millions.csv")

  // Creates a session-scoped temporary view
  df.createOrReplaceTempView("geodata")

  df.printSchema()

  // Executes the query
  val smallCountries = spark.sql("select * from geodata where yr1980 < 1 order by 2 limit 5")

  // Displays the new dataframe
  smallCountries.show(10, truncate = false)
}
