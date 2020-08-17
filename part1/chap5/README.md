Building a simple app for deployment
=====================

## 求PI的近似值

## Local mode

local mode 中，所有组件运行在一个节点。

```scala
val spark = SparkSession.builder()
.appName("My application")
.master("local")
.getOrCreate()
```

你可以指定线程的个数：

```scala
val spark = SparkSession.builder()
.appName("My application")
.master("local[2]")
getOrCreate()
```

默认地，本地模式仅运行一个线程。

## Cluster mode

实际上就是多节点执行。

不同的是，

你需要将Jar submit 到 spark 集群上执行。

## Interactive mode

交互模式提供了python、R、Scala脚本支持。你可以简单地使用Shell界面操作。这种模式下你可以使用Jupyter或Zeppelin这些数据科学工具。

这种模式包含了local mode和cluster mode，仅取决于你的启动参数。例如以本地模式开启，只需要：

```shell script
$ ./spark-shell
```

如果想要以集群模式启动，则在命令行带上`--master <Master's UL>`即可。更多参数可以通过`--help`参数参阅。


## Summary

- Spark不收集数据也可以工作；它可以自己生成数据。
- Spark支持三种执行模式：local mode, cluster mode, interactive mode.
- local mode 通常用于本地开发使用。
- cluster mode 多用于产品环境。
- 集群中的master节点知道worker节点的位置。
- 集群模式下，任务的调度执行由worker完成。
- Spark会将提交的jar分发到其它worker节点。
- 分布式系统中，MapReduce是一个常见的大数据方法。
- CICD(Continuous integration and continuous delivery)是一种敏捷方法论，它鼓励频繁的集成和交付。


