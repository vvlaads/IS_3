package lab.database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DBCPDataSourceProvider {
    private BasicDataSource ds;

    @PostConstruct
    void init() {
        ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/labs");
        ds.setUsername("postgres");
        ds.setPassword("password");

        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public BasicDataSource getDataSource() {
        return ds;
    }

    @PreDestroy
    void shutdown() throws Exception {
        ds.close();
    }
}
