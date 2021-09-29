package com.sample;

import com.datastax.oss.driver.api.core.CqlSession;
import com.simba.cassandra.jdbc.jdbc42.S42Connection;
import com.simba.cassandra.jdbc42.DataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.sql.SQLException;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

@Slf4j
public class LiquibaseApp {
    public static final String ROOT_CHANGESET_FILE = "cassandra-scheme/main.xml";
    public static final String DATABASE_NAME = "sample";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, LiquibaseException {
        try (CqlSession cqlSession = CqlSession
                .builder()
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .withLocalDatacenter("datacenter1")
                .withAuthCredentials("user", "pass")
                .build()) {
            cqlSession.execute(createKeyspace(DATABASE_NAME)
                    .ifNotExists()
                    .withSimpleStrategy(1).build());
        }

        var connectionString =
                "jdbc:cassandra://localhost:9042;DefaultKeyspace=" + DATABASE_NAME +
                        ";AuthMech=1;UID=user;PWD=pass";
        Class.forName("com.simba.cassandra.jdbc42.Driver");
        var ds = new DataSource();
        ds.setURL(connectionString);
        S42Connection connection = (S42Connection) ds.getConnection();
        var databaseInstance = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        databaseInstance.setDefaultSchemaName(DATABASE_NAME);
        var liquibase = new Liquibase(ROOT_CHANGESET_FILE, new ClassLoaderResourceAccessor(), databaseInstance);
        liquibase.update(new Contexts(), new LabelExpression());
        connection.getDSIConnection().close();
        connection.close();

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    log.info("----------------------------");
                    log.info("Threads prevent to stop jvm:");
                    Thread.getAllStackTraces().keySet().stream()
                            .filter(t -> !t.isDaemon())
                            .map(t -> t.getId() + ":" + t.getName())
                            .forEach(log::info);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


}
