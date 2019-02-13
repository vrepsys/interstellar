package cc.interstellar.db;

import java.sql.Connection;

public interface ConnectionSource {

    Connection getConnection();
}
