Transforming entire documents
=====================

Spark进行数据转换的核心是使用静态函数。

| Group | Description | Example of functions |
|:-----:|:-----------:|:--------------------:|
| Array | 操作列存储的数组 | `element_at()`, `greatest()` |
| Conversion | 将数据从一种格式转换为另一种格式 | `from_json()` |
| Date | 日期处理 | `add_months()`, `hour()`, `next_day()` |
| Mathematics | 数学相关 | `acos()`, `exp()`, `hypot()`, `rand()` |
| Security | 密码相关 | `md5()`, `hash()`, `sha2()` |
| Streaming | 流处理 | `lag()`, `row_number()` |
| String | 字符处理 | `lpad()`, `ltrim()`, `regexp_extract()`, `upper()` |
| Technical | spark自身数据信息 | `spark_partition_id()` |


## Summary

- Apache Spark is a great tool for building data pipelines using transformations.
- Spark can be used to flatten JSON documents
- You can build nested documents between two (or more) dataframes.
- Static functions are key to data transformation.
