# This project was created to reproduce the issue that happens with liquibase-cassandra
Liquibase cassandra update running from java code never ends - number of non daemon threads prevent jvm to stop. 

## Steps to reproduce

1. Run cassandra on localhost
   ```docker run --name sample-cassandra --network sample-network -d -p9042:9042 cassandra:3```
2. Download Simba JDBC Driver for Apache Cassandra as described
   at https://www.liquibase.org/blog/running-liquibase-apache-cassandra, unzip it and save CassandraJDBC42.jar to root
   of project directory
3. Run application either from Intellij Idea run configuration (main method in com.sample.LiquibaseApp) or with maven: ```mvn compile exec:java```

## Actual result
Program never ends because of number of living non-deamon threads, log:
```text
[main] INFO com.datastax.oss.driver.internal.core.DefaultMavenCoordinates - DataStax Java driver for Apache Cassandra(R) (com.datastax.oss:java-driver-core) version 4.8.0
[s0-admin-0] INFO com.datastax.oss.driver.internal.core.time.Clock - Using native clock for microsecond precision
[s0-io-2] WARN com.datastax.oss.driver.api.core.auth.PlainTextAuthProviderBase - [] localhost/127.0.0.1:9042 did not send an authentication challenge; This is suspicious because the driver expects authentication
[s0-io-3] WARN com.datastax.oss.driver.api.core.auth.PlainTextAuthProviderBase - [] localhost/127.0.0.1:9042 did not send an authentication challenge; This is suspicious because the driver expects authentication
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Sep 29, 2021 10:55:14 AM liquibase.ext
INFO: No DATABASECHANGELOGLOCK available in cassandra.
Sep 29, 2021 10:55:14 AM liquibase.ext
INFO: successfully.acquired.change.log.lock
Sep 29, 2021 10:55:14 AM liquibase.ext
WARNING: expecting exactly 1 table with name DATABASECHANGELOG, got 0
Sep 29, 2021 10:55:14 AM liquibase.ext
INFO: Creating database history table with name: sample.DATABASECHANGELOG
Sep 29, 2021 10:55:14 AM liquibase.ext
INFO: Reading from sample.DATABASECHANGELOG
Sep 29, 2021 10:55:15 AM liquibase.changelog
INFO: SQL in file scripts/sample_table.cql executed
Sep 29, 2021 10:55:15 AM liquibase.changelog
INFO: ChangeSet cassandra-scheme/changesets/sample-table-init-1.0.xml::create_sample_table::me ran successfully in 65ms
Sep 29, 2021 10:55:15 AM liquibase.ext
INFO: Successfully released change log lock
[Thread-35] INFO com.sample.LiquibaseApp - ----------------------------
[Thread-35] INFO com.sample.LiquibaseApp - Threads prevent to stop jvm:
[Thread-35] INFO com.sample.LiquibaseApp - 80:cluster3-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 127:cluster5-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 120:cluster4-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 123:cluster4-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 146:cluster7-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 184:DestroyJavaVM
[Thread-35] INFO com.sample.LiquibaseApp - 135:cluster6-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 114:cluster3-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 121:cluster4-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 82:cluster3-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 124:cluster5-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 142:cluster7-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 183:Thread-35
[Thread-35] INFO com.sample.LiquibaseApp - 125:cluster5-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 136:cluster6-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 137:cluster6-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 143:cluster7-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 133:cluster6-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 116:cluster3-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 139:cluster6-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 144:cluster7-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 140:cluster7-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 115:cluster3-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 138:cluster6-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 122:cluster4-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 117:cluster4-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 130:cluster5-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 119:cluster4-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 118:cluster4-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 180:cluster3-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 141:cluster7-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 128:cluster5-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 83:cluster3-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 129:cluster5-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 81:cluster3-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 181:cluster5-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 126:cluster5-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 145:cluster7-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 134:cluster6-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 179:cluster4-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - ----------------------------
[Thread-35] INFO com.sample.LiquibaseApp - Threads prevent to stop jvm:
[Thread-35] INFO com.sample.LiquibaseApp - 80:cluster3-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 127:cluster5-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 120:cluster4-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 123:cluster4-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 146:cluster7-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 184:DestroyJavaVM
[Thread-35] INFO com.sample.LiquibaseApp - 135:cluster6-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 114:cluster3-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 121:cluster4-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 82:cluster3-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 124:cluster5-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 142:cluster7-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 183:Thread-35
[Thread-35] INFO com.sample.LiquibaseApp - 125:cluster5-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 136:cluster6-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 137:cluster6-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 143:cluster7-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 133:cluster6-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 116:cluster3-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 139:cluster6-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 144:cluster7-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 140:cluster7-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 115:cluster3-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 138:cluster6-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 122:cluster4-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 117:cluster4-connection-reaper-0
[Thread-35] INFO com.sample.LiquibaseApp - 130:cluster5-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 119:cluster4-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 118:cluster4-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 180:cluster3-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 141:cluster7-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 128:cluster5-nio-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 83:cluster3-nio-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 129:cluster5-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 81:cluster3-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 181:cluster5-worker-1
[Thread-35] INFO com.sample.LiquibaseApp - 126:cluster5-scheduled-task-worker-0
[Thread-35] INFO com.sample.LiquibaseApp - 145:cluster7-nio-worker-2
[Thread-35] INFO com.sample.LiquibaseApp - 134:cluster6-timeouter-0
[Thread-35] INFO com.sample.LiquibaseApp - 179:cluster4-worker-1
```

## Expected result
Program ends normally after liquibase schema update