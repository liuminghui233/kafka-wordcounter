# kafka-wordcounter

参考 APACHE KAFKA QUICKSTART (http://kafka.apache.org/quickstart)与 APACHE KAFKA DOCUMENTATION (http://kafka.apache.org/documentation/) 完成 Kafka 的安装和使用，并编程实现以下功能：

（1）Producer 从文件读取文本内容，发送至 Kafka ；

（2）Consumer 从 Kafka 接收消息，对不同单词进行计数，并将最终计数结果输出到文件。  


1. 编译代码。

   ```bash
   $ mvn clean package
   ```

2. 开启 Kafka 。

   进入 Kafka 所在目录，执行以下命令：

   ```bash
   # Start the ZooKeeper service
   # Note: Soon, ZooKeeper will no longer be required by Apache Kafka.
   $ bin/zookeeper-server-start.sh config/zookeeper.properties
   
   # Start the Kafka broker service
   $ bin/kafka-server-start.sh config/server.properties
   ```

3. 生产者发送消息。

   运行：

   ```bash
   $ mvn exec:java  -Dexec.mainClass="MyProducer" -Dexec.args="./text"
   ```

4. 消费者接收消息，并进行单词计数。

   运行：

   ```bash
   $ mvn exec:java -Dexec.mainClass="MyConsumer"
   ```
   点击 Ctrl + C 停止运行。
