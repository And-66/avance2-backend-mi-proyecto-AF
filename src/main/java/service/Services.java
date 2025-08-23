/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import service.jdbc.BancoServiceJdbc;
import service.remote.BancoServiceRemoteProxy;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author XPC
 */
public final class Services {
    private static IBancoService INSTANCE;

    private Services() {}

    public static synchronized IBancoService service() {
        if (INSTANCE != null) return INSTANCE;

        Properties p = new Properties();
        try (InputStream in = Services.class.getResourceAsStream("/remote.properties")) {
            if (in != null) p.load(in);
        } catch (Exception ignored) {}

        String mode = p.getProperty("mode", "local").trim().toLowerCase();
        if ("remote".equals(mode)) {
            String host = p.getProperty("host", "127.0.0.1");
            int port = Integer.parseInt(p.getProperty("port", "9090"));
            INSTANCE = new BancoServiceRemoteProxy(host, port);
        } else {
            INSTANCE = BancoServiceJdbc.getInstance();
        }
        return INSTANCE;
    }
}

