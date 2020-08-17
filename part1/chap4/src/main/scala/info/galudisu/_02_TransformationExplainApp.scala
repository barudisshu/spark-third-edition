package info.galudisu

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr

object _02_TransformationExplainApp extends App {

  val spark = SparkSession.builder().appName("Showing execution plan").master("local").getOrCreate()

  var df = spark.read
    .format("csv")
    .option("header", "true")
    .load("data/NCHS_-_Teen_Birth_Rates_for_Age_Group_15-19_in_the_United_States_by_County.csv")

  val df0 = df

  df = df.union(df0)

  df = df.withColumnRenamed("Lower Confidence Limit", "lcl")
  df = df.withColumnRenamed("Upper Confidence Limit", "ucl")

  df = df
    .withColumn("avg", expr("(lcl+ucl)/2"))
    .withColumn("lcl2", df.col("lcl"))
    .withColumn("ucl2", df.col("ucl"))

  // Prints the physical plan to the console for debugging purposes.
  df.explain("formatted")
}
