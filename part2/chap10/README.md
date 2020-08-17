Ingestion through structured streaming
=====================


## Summary

- Spark session 提供批处理和流处理两种模式
- 流处理实际上是一个迷你的批处理方式
- 流处理的实现使用`readStream()`方法，带有`start()`方法开始执行
- 流处理的查询对象`StreamingQuery`，用于流处理的查询。
- 流数据被存储在结果表中。
- 你需要使用`awaitTermination()`方法等待数据流的数据进来，它是个阻塞方法，另外还提供了`isActive()`检测查询是否可用。
- 同一时间你可以读取多个数据源。

