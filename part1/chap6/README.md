Deploying your simple app
=====================

本章介绍spark的部署，安装、submit jar并执行.

本工程仅使用SBT，请自行学习基础知识。

## 如何提交Jar

首先进入到master节点的目录。

```shell script
$ cd /opt/apache-spark/bin
```

提交JAR。

```shell script
$ ./spark-submit \
--class net.jgp.books. spark.ch05.lab210.piComputeClusterSubmitJob.PiComputeClusterSubmitJobApp \
--master "spark://un:7077" \
<path to>/spark-chapter05-1.0.0-SNAPSHOT.JAR
```

控制台将输出相应的执行日志信息。

另外一种方式是，你可以直接使用Maven、SBT等构建工具运行。前提是，你需要在master节点上执行命令，并且master节点必须是可访问的。

```shell script
$ mvn clean install exec:exec
```

或

```shell script
$ sbt run
```

## 如何安装部署

[略]

## Summary

- Spark 提供了一个Web UI用来分析job和application的执行情况。
