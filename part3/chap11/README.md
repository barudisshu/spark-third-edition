Working with SQL
=====================

## Working with Spark SQL

Spark 提供了一种类比SQL查询的能力。包括：视图、表达式、排序、联结查询等(但不能drop和update)。


## Summary

- Spark 支持SQL查询
- 因为数据是不可变的，你不能drop或modify记录；你需要重新创建一个新的dataset。
- 要删除dataframe中的记录，你需要构建一个新的基于filtered机制的dataframe