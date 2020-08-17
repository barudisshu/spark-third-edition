Transforming entire documents
=====================

UDFs, user-defined functions.用户自定义函数

## Registering the UDF with Spark

Spark 允许创建自定义函数.

```scala
spark.udf().register("isOpen", new IsOpenUdf(), DataTypes.BooleanType)
```


