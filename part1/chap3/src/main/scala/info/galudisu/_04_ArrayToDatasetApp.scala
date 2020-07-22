package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

/**
  * 通过字符串来创建dataframe
  */
object _04_ArrayToDatasetApp extends App with LazyLogging {

  implicit val encoders: Encoder[String] = Encoders.STRING

  // Creates a Spark session
  val spark = SparkSession
    .builder()
    .appName("Array to Dataset<String>")
    .master("local")
    .getOrCreate()

  // Creates a static array with four values
  val stringList = List("Jean", "Liz", "Pierre", "Lauric")
  var ds         = spark.createDataset(stringList)

  ds.show()
  ds.printSchema()

  // 转换为dataframe
  val df = ds.toDF()
  df.show()
  df.printSchema()
}
