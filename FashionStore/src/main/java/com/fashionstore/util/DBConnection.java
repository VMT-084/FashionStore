
package com.fashionstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String HOST =
            System.getenv("DB_HOST");

    private static final String PORT =
            System.getenv("DB_PORT");

    private static final String DATABASE =
            System.getenv("DB_NAME");

    private static final String USERNAME =
            System.getenv("DB_USER");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/"
            + DATABASE
            + "?sslMode=VERIFY_IDENTITY";

    public static Connection getConnection() throws SQLException {

        if (HOST == null || PORT == null || DATABASE == null
                || USERNAME == null || PASSWORD == null) {
            throw new SQLException(
                    "Database environment variables are missing.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
