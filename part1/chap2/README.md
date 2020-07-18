Architecture and flow
=============

1. Building your mental model
2. Using Java code to build your mental model
3. Walking through your application

## Walking through your application

Spark应用的执行有一些通用的步骤。

1. 连接到一个集群

任何一个Spark应用。首先得连接到Spark master 并 获得一个 Spark `_session_`。改操作每次都是要做的。

```scala
val spark = SparkSession.builder
    .appName("CSV to DB")
    .master("local")
    .getOrCreate();
``` 

> ## Method chaining makes Java more compact
>
> Java中有链式方法调用的形式`SparkSession.builder().appName(...).getOrCreate()`。它实际上是一种构造模式。

> ## Local mode is not a cluster, but it's much easier
>
> 如其在集群上跑，不如在本地调试。
>

2. 加载，收集CSV文件

该步骤中，Spark的master会告知worker去加载文件。文件会被存储在分布式文件系统上(例如HDFS)。其中分区`partition`是worker的专用内存区域。

```scala
var df = spark.read
    .format("csv")
    .option("header", "true")
    .load("data/authors.csv")
```

每个worker会创建task去读取文件，并为task分配内存分区。读取的文件记录被分配在不同节点的内存分区(memory partition)上。


3. 转换数据

```scala
df = df.withColumn("name", concat(df.col("lname"), lit(", "), df.col("fname")))
```

4. 将dataframe保存到数据库

```scala
val dbConnectionUrl = "jdbc:postgresql://localhost/spark_labs"
val prop = new Properties()
prop.setProperty("driver", "org.postgresql.Driver")
prop.setProperty("user", "jgp")
prop.setProperty("password", "Spark<3Java")
df.write()
.mode(SaveMode.Overwrite)
.jdbc(dbConnectionUrl, "ch02", prop);
```

由于数据集被分配在不同节点的不同分区上，整个dataframe需要由work连接到数据库。也就是说，有多少个worker就会有多少个连接。并由worker将分区记录写入。

## Summary

- Your application is the driver. Data may not have to come to the driver; it can be driven remotely. It is important to remember this when you size your deployment.
- The driver connects to a master and gets a session. Data will be attached to this session; the session defines the life cycle of the data on the worker's nodes.
- The master can be local (your local machine) or a remote cluster. Using the local mode will not require you to build a cluster, making your life much easier while you are developing.
- Data is partitioned and processed within the partition. Partitions are in memory.
- Spark can easily read from CSV files.
- Spark can easily save data in relational databases.
- Spark is lazy: it will work only when you ask it to do so via an action. This laziness is good for you, and chapter 4 provides more details.
- Spark's APIs rely heavily on method chaining.
