package info.galudisu

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object _01_MySQLToDatasetApp extends App with LazyLogging {

  val spark =
    SparkSession.builder().appName("MySQL to Dataframe using a JDBC Connection").master("local[*]").getOrCreate()

  val props = new Properties()
  props.put("user", "root")
  props.put("password", "root")
  props.put("useSSL", "false")

  val df =
    spark.read.jdbc("jdbc:mysql://localhost:3306/sakila?serverTimezone=EST", "actor", props).orderBy("last_name")

  df.show(5)
  df.printSchema()

  logger.debug("*** The dataframe contains {} records(s).", df.count())

  spark.stop()
}
