package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes

/**
  * 本地视图(local view)和全局视图(global view)的不同在于：当session结束后，本地视图会被移除；而全局视图仅在所有session都结束情况下才被移除。
  */
object _02_SimpleSelectGlobalViewApp extends App with LazyLogging {

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

  df.createOrReplaceGlobalTempView("geodata")

  val smallCountries = spark.sql("select * from global_temp.geodata where yr1980 < 1 order by 2 limit 5")

  // Displays the new dataframe
  smallCountries.show(10, truncate = false)

  spark.stop()
}
