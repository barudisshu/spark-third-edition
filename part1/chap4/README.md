Fundamentally lazy
=====================

"lazy", 惰性求值。

"Catalyst", spark 内置转换优化器。

Spark 仅仅 _创建配方(create the recipe)_。并由action 执行这些配方。这就是spark为"lazy"的由来。

> What do I mean by recipe? Is it a job?
> 什么是“配方”？
> 在法语，食品的关系、以及食品的相关术语数不胜数。譬如术语 `mise en place，准备食材、准备舞台...`：在烹饪之前，需要要把所有原料都配好，剪、剥、切、磨...以此类推，你会将收集数据说为`mise en place`。
> 总而言之：Spark 处理工作，每个工作包含适当数量的由“配方”组成的转换(transformations)。

Spark 对 “配方” 实现了 DAG(directed acyclic graph，有向循环图)

有向循环图来自于数学和计算科学，更多细节参考[维基](https://en.wikipedia.org/wiki/Directed_acyclic_graph)

关于Spark可视化的更多信息，参考[这里](https://databricks.com/blog/2015/06/22/understanding-your-spark-application-through-visualization.html)

## Summary

- Spark is efficiently lazy.首先它会构建一系列的DAG转换，并被Catalyst优化，Catalyst为Spark的内置优化器。
- dataframe内的转换的数据是不可修改的。
- dataframe提供了action，transformation将被执行，数据被修改。
- spark工作在column level，不需要迭代处理上面的数据。
- 可以使用`explain()`方法打印查询的计划。