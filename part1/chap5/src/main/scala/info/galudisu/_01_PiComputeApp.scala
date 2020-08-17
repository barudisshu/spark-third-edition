package info.galudisu

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

trait _01_PiComputeApp {}

/**
  * PI 圆周率计算[近似值]
  *
  * 勾股定理近似法
  */
object _01_PiComputeApp extends App with _01_PiComputeApp with LazyLogging {

  val numberOfThrows = 100000 * 10

  logger.debug("*** About to  throw {} darts, ready? Stay away from the target!", numberOfThrows)

  val t0 = System.currentTimeMillis()

  // Uses all the possible threads no this system
  val spark = SparkSession.builder().appName("Spark Pi").master("local[*]").getOrCreate()
  import spark.implicits._

  val t1 = System.currentTimeMillis()

  logger.debug("*** Session initialized in {} ms", t1 - t0)

  val listOfThrows = Range(0, numberOfThrows)

  val incrementalDf = spark.createDataset(listOfThrows).toDF

  val t2 = System.currentTimeMillis()

  logger.debug("*** Initial dataframe built in {} ms", t2 - t1)

  // 随机任意坐标点
  val dartsDs = incrementalDf.map(func = _ => {
    val x = Math.random() * 2 - 1
    val y = Math.random() * 2 - 1
    // 根据三角勾股定理：x^2 + y^2 = r^2
    val radius = x * x + y * y
    // 如果半径小于或等于1，说明坐标点落在圆内；否则圆外
    if (radius <= 1) 1 else 0
  })

  val t3 = System.currentTimeMillis()

  logger.debug("*** Throwing darts done in {} ms", t3 - t2)

  // 获取圆内记录点, 所有点之和即为扇形的面积
  val dartsInCircle = dartsDs.reduce(_ + _)

  val t4 = System.currentTimeMillis()

  logger.debug("*** Analyzing result in {} ms", t4 - t3)

  // numberOfThrows 实际为 r^2。即矩形面积。π = 扇形面积 ÷ 半径平方 = 4 x dartsInCircle / numberOfThrows
  logger.debug("*** Pi is roughly {}", 4.0 * dartsInCircle / numberOfThrows)

  spark.stop()

}
