/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import exception.AutenticacionException;
import exception.FondosInsuficientesException;
import model.Cliente;
import service.jdbc.BancoServiceJdbc;
import service.remote.protocol.*;
import service.remote.protocol.dto.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;

/**
 *
 * @author XPC
 */
final class ServerWorker implements Runnable {
    private final Socket s;
    private final BancoServiceJdbc svc;

    ServerWorker(Socket s, BancoServiceJdbc svc) {
        this.s = s; this.svc = svc;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(s.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {

            Request req = (Request) in.readObject();
            Response res = new Response(); res.ok = true;

            try {
                switch (req.op) {
                    case LOGIN -> {
                        LoginReq lr = (LoginReq) req.payload;
                        var cuenta = svc.loginPorPin(lr.numero, lr.pin);
                        var c = cuenta.getCliente();
                        res.data = new LoginResp(
                                c.getIdCliente(), cuenta.getNumCuenta(),
                                c.getNombre(), c.getApellido(), c.getDireccion(),
                                c.getTelefono(), c.getEmail(),
                                BigDecimal.valueOf(cuenta.getBalance())
                        );
                    }
                    case ABRIR_CUENTA -> {
                        AbrirCuentaReq ar = (AbrirCuentaReq) req.payload;
                        Cliente cli = new Cliente(0, ar.nombre, ar.apellido, ar.direccion, ar.telefono, ar.email);
                        var cuenta = svc.abrirCuenta(cli, ar.pin);
                        res.data = new CuentaNuevaResp(cuenta.getNumCuenta(), BigDecimal.valueOf(cuenta.getBalance()));
                    }
                    case DEPOSITO -> {
                        MontoReq mr = (MontoReq) req.payload;
                        res.data = svc.depositar(mr.numero, mr.monto);
                    }
                    case RETIRO -> {
                        MontoReq mr = (MontoReq) req.payload;
                        res.data = svc.retirar(mr.numero, mr.monto);
                    }
                    case TRANSFERENCIA -> {
                        TransferenciaReq tr = (TransferenciaReq) req.payload;
                        res.data = svc.transferir(tr.origen, tr.destino, tr.monto);
                    }
                    case HISTORIAL_BASICO -> {
                        NumeroReq nr = (NumeroReq) req.payload;
                        res.data = svc.listarHistorialBasico(nr.numero);
                    }
                    case ULTIMA_TX_BASICA -> {
                        NumeroReq nr = (NumeroReq) req.payload;
                        res.data = svc.ultimaTransaccionBasica(nr.numero);
                    }
                    case CONSULTAR_SALDO -> {
                        NumeroReq nr = (NumeroReq) req.payload;
                        res.data = svc.consultarSaldo(nr.numero);
                    }
                    
                    case ACTUALIZAR_CLIENTE -> {
                        ActualizarClienteReq ar = (ActualizarClienteReq) req.payload;
                        var cli = new model.Cliente(ar.idCliente, ar.nombre, ar.apellido,
                                                    ar.direccion, ar.telefono, ar.email);
                        svc.actualizarCliente(cli);
                        res.data = null;
                    }
                    case ACTUALIZAR_CUENTA -> {
                        ActualizarCuentaReq ar = (ActualizarCuentaReq) req.payload;
                        var dummy = new model.Cliente(0, "", "", "", "", "");
                        var cuenta = new model.Cuenta(ar.numero, dummy, ar.pin, 0.0);
                        svc.actualizarCuenta(cuenta);
                        res.data = null;
                    }
                }
            } catch (AutenticacionException | FondosInsuficientesException e) {
                res.ok = false; res.error = e.getMessage();
            } catch (Exception e) {
                res.ok = false; res.error = "Error interno: " + e.getMessage();
            }

            out.writeObject(res);
            out.flush();

        } catch (Exception ignored) {
        }
    }
}
