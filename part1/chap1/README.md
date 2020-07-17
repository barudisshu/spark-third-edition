So, What is Spark, anyway?
=============

1. The big picture: What Spark is and what it does
2. How can you use Spark?
3. What can you do with Spark?
4. Why you will love the dataframe
5. Your first example

## The four pillars of mana

Spark 的核心部分(mana, 魔力)

- Spark SQL

  Spark SQL 提供了类似RDBMS的数据处理能力。

- Spark Streaming
  
  流数据和批数据处理
  
- Spark MLlib(for machine learning)

  机器学习

- GraphX

  图数据结构(graph data structures)

## Spark in a data processing/engineering scenario

1. Ingestion (收集)
2. Improvement of data quality (DQ) (提取)
3. Transformation (转换)
4. Publication (存储)

每个步骤之后，数据会停留在一个区域(zone).

- Ingestion - 收集大的数据。不同格式、不同结构、不同来源。该阶段的数据称为`原生数据(raw data)`。该区域被称为 `staging, landing, bronze` 又或 `swamp zone`。

- Improving data quality(DQ) - 在处理数据之前，更多需要检测数据的质量。例如一个DQ例子：你需要检查所有出生日都是过去时。另外，你还可以在此步骤对数据进行混淆(obfuscate some data)：例如你在一个卫生保健环境(health-care environment) 处理SSNs(Social Security numbers，美国的个人社会安全号)，你可以确保这些数据是不可访问的或授权访问。当该阶段的数据提取后(refined).该区域称为`pure data` 。也被称为 `refinery, silver, pond, sandbox`或`exploration zone`

- Transforming data - 顾名思义。该步骤中你可以加入其它数据集、实现自定义函数、处理聚集(aggregations)、实现机器学习、等等。改步骤的目的是获得有价值的数据，`rich data`， 数据分析的成果。该阶段的区域称为 ` production, gold, refined, lagoon` 或 `operationalization zone`.

- Loading and publishing` - 作为一个ETL(Extract,transform,and load is a classic data warehouse process)流程，你可以将数据加载到数据仓库中完成你的工作了。你可以使用商业化工具、也可以简单写入文件、主要取决于你的业务。



## Spark in a data science scenario

数据科学又是另外一个领域，区别于软件开发工程。数据科学家不关心软件实现的底层，他们更多的使用Notebook这类的工具。notebook包括有Jupyter, Zeppelin, IBM Watson Studio, Databricks Runtime。

## What can you do with Spark?

机器学习、人工智能、等等。

预测、

生物科技、种子、病毒

公安追踪、犯罪心理



## Dataframe

dataframe 的概念是Spark的本质。下面会从Java层面和RDBMS层面对比介绍

### Java perspective

如果你熟悉JDBC，dataframe类似于`ResultSet`。它包含数据，拥有API接口...

-  数据通过API访问
- 可以访问Schema

不同的是：

- 不是通过`next()`方法访问的。
- API可以通过用户自定义进行扩展(UDFs, user-defined functions)。UDFs会在后面章节学习到。
- 访问数据先通过`Row`然后再到`Column`。
- 元数据是最基本的结构，在Spark中不会有什么主键、外键、索引这些东西。

### RDBMS perspective

dataframe类比数据库而已，有下面相似的地方：

- 数据由行和列表述
- 列(Columns)的数据结构是强类型。(第二范式)

不同的是：

- 数据可以内嵌，可以作为JSON或XML文档。
- 不能更新或删除行；仅可以创建新的dataframes
- 可以简单地添加或删除列。
- 没有约束(constraints)、没有索引、没有主键或外键、没有触发器、更不可能有其它的(存储过程、视图...)。


## 如何运行

>Spark可以不需要安装集群环境下进行开发测试，在windows环境下需要额外添加`HADOOP_HOME` 并制定PATH=$HADOOP_HOME/bin，以确保相应的运行以来。

可以使用你喜欢的任何构建工具，例如maven、gradle、sbt等。由于Spark使用scala编写，这里使用SBT multi project构建项目工程。

## Summary

Spark是一个分析系统；以分布式的方式处理工作任务和算法问题。不仅仅用于分析。

Spark支持SQL、Java、Scala、R、Python多种编程语言接口。

Spark的主要存储数据是dataframe。dataframe组合了使用API存储的能力。

类似于JDBC的`ResultSet`。

dataframe的实现于`Dataset<Row>`。

Spark的算法不局限于MapReduce。它的API允许大量的算法作用于数据。

Spark Streaming经常被用于企业，商业中更多是实时数据分析(real-time analytics)。

分析已经从简单的聚集进化了。企业希望通过计算获得用户的期望信息。Spark提供了机器学习和深度学习满足这种需求。

Graph是一种特殊的分析案例. Spark提供了对该分析的支持。

