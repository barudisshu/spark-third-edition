package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr

trait _01_TransformationAndActionApp {

  val NOOP: String = "noop"
  val FULL: String = "full"
}

/**
  * 定义3种actions：
  * - `noop` no operation/transformation
  * - `col` column creation
  * - `full` full process
  */
object _01_TransformationAndActionApp extends _01_TransformationAndActionApp with LazyLogging {

  def main(args: Array[String]): Unit = {

    // initial mode
    val mode = Option(x = args.length match {
      case 0 => None
      case 1 => Some(args(0))
    }).flatten.getOrElse(NOOP)

    // Sets the timer
    val t0 = System.currentTimeMillis()

    // Creates the sessions
    val spark = SparkSession.builder().appName("Analysing Catalyst's behavior").master("local").getOrCreate()

    // Measures the time sent creating the session
    val t1 = System.currentTimeMillis()

    logger.debug("1. Creating a session ............ {}", t1 - t0)

    // Reads the file
    var df = spark.read
      .format("csv")
      .option("header", "true")
      .load("data/NCHS_-_Teen_Birth_Rates_for_Age_Group_15-19_in_the_United_States_by_County.csv")

    // Creates a reference dataframe to use in the copy
    val initalDf = df

    // Measures the time spent reading the file and creating the dataframe
    val t2 = System.currentTimeMillis()

    logger.debug("2. Loading initial dataset ....... {}", t2 - t1)

    // 组合成一个大的数据集，用于测量优化器的功能
    for (_ <- Range(0, 60)) {
      df = df.union(initalDf)
    }

    // Measures the time needed to build a bigger dataset
    val t3 = System.currentTimeMillis()

    logger.debug("3. Building full dataset ......... {}", t3 - t2)

    // Basic cleanup: makes the column names shorter for easier manipulation
    df = df.withColumnRenamed("Lower Confidence Limit", "lcl")
    df = df.withColumnRenamed("Upper Confidence Limit", "ucl")

    // Measures the time for cleanup
    val t4 = System.currentTimeMillis()

    logger.debug("4. Clean-up ...................... {}", t4 - t3)

    // The actual data transformation
    // If mode is "noop," skips all transformations; otherwise, creates new columns

    if (mode.equalsIgnoreCase(NOOP)) {
      df = df
        .withColumn("avg", expr("(lcl+ucl)/2"))
        .withColumn("lcl2", df.col("lcl"))
        .withColumn("ucl2", df.col("ucl"))
    }

    if (mode.equalsIgnoreCase(FULL)) {
      df = df
        .drop(df.col("avg"))
        .drop(df.col("lcl2"))
        .drop(df.col("ucl2"))
    }

    val t5 = System.currentTimeMillis()

    logger.debug("5. Transformations ............... {}", t5 - t4)

    // Performs the collection action
    df.collect()

    // Measures the time needed for the action
    val t6 = System.currentTimeMillis()

    logger.debug("6. Final action .................. {}", t6 - t5)

    logger.debug("# of records ..................... {}", df.count())

  }
}
