/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 *
 * @author XPC
 */
public final class ConnectionFactory {
    private static final Properties P = new Properties();

    static {
        try {
            var in = ConnectionFactory.class.getResourceAsStream("/db.properties");
            if (in == null) throw new IllegalStateException("No se encontró db.properties en resources");
            P.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando ConnectionFactory", e);
        }
    }

    private ConnectionFactory() {}

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(
                P.getProperty("url"),
                P.getProperty("user"),
                P.getProperty("password"));
    }
}
