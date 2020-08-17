package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes

object _02_ComplexCsvToDataframeWithSchemaApp extends App with LazyLogging {

  val spark = SparkSession.builder().appName("Complex csv with a schema to Dataframe").master("local[*]").getOrCreate()

  val schema = DataTypes.createStructType(
    Array(
      DataTypes.createStructField("id", DataTypes.IntegerType, false),
      DataTypes.createStructField("authorId", DataTypes.IntegerType, true),
      DataTypes.createStructField("bookTitle", DataTypes.StringType, false),
      DataTypes.createStructField("releaseDate", DataTypes.DateType, true),
      DataTypes.createStructField("url", DataTypes.StringType, false),
    )
  )

  val df = spark.read
    .format("csv")
    .option("header", "true")
    .option("multiline", "true")
    .option("sep", ";")
    .option("dateFormat", "MM/dd/yyyy")
    .option("quote", "*")
    .schema(schema)
    .load("data/books2.csv")

  df.show(5, 15)
  df.printSchema()

  spark.stop()

}
