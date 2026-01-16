package lab.database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
public class DBCPDataSourceProvider implements Serializable {
    private BasicDataSource ds;

    @PostConstruct
    void init() {
        ds = new BasicDataSource();
        ds.setUrl(System.getenv("DB_URL"));
        ds.setUsername(System.getenv("DB_USER"));
        ds.setPassword(System.getenv("DB_PASSWORD"));
        ds.setDriverClassName("org.postgresql.Driver");

        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxTotal(20);
        ds.setMaxOpenPreparedStatements(100);
        ds.setValidationQuery("SELECT 1");
        ds.setTestOnBorrow(true);
    }

    public BasicDataSource getDataSource() {
        return ds;
    }

    @PreDestroy
    void shutdown() throws Exception {
        ds.close();
    }
}
