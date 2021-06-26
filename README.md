# 安装

## 单机

### 启动命令

```sh
nohup sh bin/mqnamesrv & #-c 指定配置文件
nohup sh bin/mqbroker -n ${内网ip}:9876 & #-c 指定配置文件
```

### 调整JVM启动参数

```sh
vim bin/runserver.sh
vim bin/runbroker.sh
```

### 停止命令

```sh
sh bin/mqshutdown broker
sh bin/mqshutdown namesrv
```

## 集群

### 服务器准备

| 服务器        | 角色            |
| ------------- | --------------- |
| 192.168.56.10 | master1、slave2 |
| 192.168.56.20 | master2、slave1 |

### 部署问题

- 设置`broker`的`brokerIP`
- 从节点和主节点在同一台服务器，需要设置不同的`sotre`路径
- 查看集群注册状态 `sh bin/mqadmin clusterList -n ${namesvr ip:port}`分号分隔

# 消费者

## 消费模式

- 负载均衡（均摊消费）
- 集群模式（收到所有的消息）

