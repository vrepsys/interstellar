package cc.interstellar.db;


import cc.interstellar.InterstellarException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class PostgreSQLConnectionSource implements ConnectionSource {

    private String url;

    private Properties props = new Properties();

    public PostgreSQLConnectionSource(String url, String user, String password) {
        this.url = url;
        props.setProperty("user", user);
        props.setProperty("password", password);
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(this.url, props);
        } catch (Exception e) {
            throw new InterstellarException(e);
        }
    }

}
