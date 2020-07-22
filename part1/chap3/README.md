The majestic role of the dataframe
===========

1. The essential role of the dataframe in Spark
2. Using dataframes through examples
3. The dataframe is `a Dataset<Row>`
4. Dataframe's ancestor: the RDD


dataframe是Spark的核心所在。

## The essential role of the dataframe in Spark

`dataframe` 既是一个数据结构又是个API。 `dataframe`的API被用在Spark SQL、Spark Streaming、MLlib和GraphX中。

1. 组织架构

`dataframe`包含三层结构：API、Implementation & Schema、Storage.如下

```
API

Implementation & Schema

Storage

```

dataframe 可以由多种资源数组构造得来，例如文件、数据库或自定义的数据源。

Storage 可以在内存或磁盘上，主要取决于Spark的策略。

2. 不可变的数据类型

`Immutability`意思就是`unchangeable`。

## Dataframe is a `Dataset<Row>`

Dataframe实际上是一个`Dataset<Row>`，

































