The theory crippled by awesome examples
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

## Dataframe





































