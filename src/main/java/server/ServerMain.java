/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import service.jdbc.BancoServiceJdbc;

import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author XPC
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try (InputStream in = ServerMain.class.getResourceAsStream("/remote.properties")) {
            if (in != null) p.load(in);
        }
        int port = Integer.parseInt(p.getProperty("port", "9090"));
        ExecutorService pool = Executors.newFixedThreadPool(32);

        var servicio = BancoServiceJdbc.getInstance();
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("[Servidor] FideBank escuchando en puerto " + port);
            while (true) {
                var socket = server.accept();
                pool.submit(new ServerWorker(socket, servicio));
            }
        }
    }
}

