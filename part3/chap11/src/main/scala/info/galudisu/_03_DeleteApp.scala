package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes

/**
  * 删除dataframe的数据
  */
object _03_DeleteApp extends App with LazyLogging {
  val spark = SparkSession.builder().appName("Simple SELECT using SQL").master("local[*]").getOrCreate()

  val schema = DataTypes.createStructType(
    Array(
      DataTypes.createStructField("geo", DataTypes.StringType, true),
      DataTypes.createStructField("yr1980", DataTypes.DoubleType, false),
      DataTypes.createStructField("yr1981", DataTypes.DoubleType, false),
      DataTypes.createStructField("yr2010", DataTypes.DoubleType, false)
    )
  )

  val df = spark.read
    .format("csv")
    .option("header", value = true)
    .schema(schema)
    .load("data/populationbycountry19802010millions.csv")

  // Creates a session-scoped temporary view
  df.createOrReplaceTempView("geodata")

  logger.debug("Territories in orginal dataset: {}", df.count())

  val cleanedDf = spark.sql(
    "select * from geodata where geo is not null and geo != 'Africa' and geo != 'North America' and geo != 'World' and geo != 'Asia & Oceania' and geo != 'Central & South Ameria' and geo != 'Europe' and geo != 'Eurasia' and geo != 'Middle East' order by yr2010 desc")

  logger.debug("Territories in cleaned dataset: {}", cleanedDf.count())
  cleanedDf.show(20, truncate = false)

  spark.stop()
}
