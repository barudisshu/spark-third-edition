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

```scala
type DataFrame = Dataset[Row]
```

## Dataframe's ancestor: the RDD

## Summary

- 一个dataframe就是一个带schema的RDD。
- dataframe就是`Dataset<Row>`。
- dataset则是期望类型的数据集——代码写为：`Dataset<String>`，`Dataset<Book>`，或`Dataset<SomePojo>`。
- dataframe可以存储栏位(columnar)信息。譬如：CSV文件、嵌套的数组、JSON文件。对于处理CSV文件、CSV文件或其它格式的API方式是一样的。
- 对于JSON格式，可以使用`.`操作访问字段。
- dataframe的[API](http://mng.bz/qXYE)文档。
- 提供的[静态方法](http://mng.bz/5AQD).
- dataset中的POJO可以直接重用。
- 如果想要将对象作为dataset的内容，必须序列化。
- dataset的`drop`方法删除列信息。
- dataset的`col()`方法返回列信息。
- `to_date()`转换日期格式的string为date。
- `expr()`函数可以使用字段名带表达式进行计算或SQL查询。
- `lit()`返回列的字面量值。
- RDD，resilient distributed dataset。是一个不可变的分布式数据集。
- 你必须基于dataframe来操作RDD。
- Catalyst是转换优化器。
- 跨平台(scala,java,R,python...)的API(graph,SQL,ML,streaming)调用都是统一的。





























