/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.remote;

import exception.AutenticacionException;
import exception.FondosInsuficientesException;
import model.Cliente;
import model.Cuenta;
import service.IBancoService;
import service.remote.protocol.*;
import service.remote.protocol.dto.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author XPC
 */
public class BancoServiceRemoteProxy implements IBancoService {
    private final String host;
    private final int port;

    public BancoServiceRemoteProxy(String host, int port) {
        this.host = host; this.port = port;
    }

    private Response call(Operacion op, Object payload) throws Exception {
        try (Socket s = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream())) {
            Request req = new Request();
            req.op = op; req.payload = payload;
            out.writeObject(req); out.flush();
            return (Response) in.readObject();
        }
    }

    @Override
    public Cuenta loginPorPin(String numCuenta, String pin) throws AutenticacionException {
        try {
            Response r = call(Operacion.LOGIN, new LoginReq(numCuenta, pin));
            if (!r.ok) throw new AutenticacionException(r.error);

            LoginResp lr = (LoginResp) r.data;
            Cliente cli = new Cliente(
                (int) lr.clienteId, lr.nombre, lr.apellido,
                lr.direccion, lr.telefono, lr.email
            );
            return new Cuenta(lr.numero, cli, pin, lr.saldo.doubleValue());
        } catch (AutenticacionException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (login)", e);
        }
    }

    @Override
    public Cuenta abrirCuenta(Cliente cli, String pin) {
        try {
            AbrirCuentaReq req = new AbrirCuentaReq(
                cli.getNombre(), cli.getApellido(), cli.getDireccion(),
                cli.getTelefono(), cli.getEmail(), pin
            );
            Response r = call(Operacion.ABRIR_CUENTA, req);
            if (!r.ok) throw new RuntimeException(r.error);

            CuentaNuevaResp cr = (CuentaNuevaResp) r.data;
            return new Cuenta(cr.numero, cli, pin, cr.saldo.doubleValue());
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (abrir cuenta)", e);
        }
    }

    @Override
    public BigDecimal depositar(String numero, BigDecimal monto) {
        try {
            Response r = call(Operacion.DEPOSITO, new MontoReq(numero, monto));
            if (!r.ok) throw new RuntimeException(r.error);
            return (BigDecimal) r.data;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (depósito)", e);
        }
    }

    @Override
    public BigDecimal retirar(String numero, BigDecimal monto) throws FondosInsuficientesException {
        try {
            Response r = call(Operacion.RETIRO, new MontoReq(numero, monto));
            if (!r.ok) {
                if (r.error != null && r.error.toLowerCase().contains("saldo"))
                    throw new FondosInsuficientesException(r.error);
                throw new RuntimeException(r.error);
            }
            return (BigDecimal) r.data;
        } catch (FondosInsuficientesException fie) {
            throw fie;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (retiro)", e);
        }
    }

    @Override
    public BigDecimal transferir(String origen, String destino, BigDecimal monto) throws FondosInsuficientesException {
        try {
            Response r = call(Operacion.TRANSFERENCIA, new TransferenciaReq(origen, destino, monto));
            if (!r.ok) {
                if (r.error != null && r.error.toLowerCase().contains("saldo"))
                    throw new FondosInsuficientesException(r.error);
                throw new RuntimeException(r.error != null ? r.error : "Error en transferencia");
            }
            return (BigDecimal) r.data;
        } catch (FondosInsuficientesException fie) {
            throw fie;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (transferencia)", e);
        }
    }

    @Override
    public BigDecimal consultarSaldo(String numero) {
        try {
            Response r = call(Operacion.CONSULTAR_SALDO, new NumeroReq(numero));
            if (!r.ok) throw new RuntimeException(r.error);
            return (BigDecimal) r.data;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (saldo)", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> listarHistorialBasico(String numero) {
        try {
            Response r = call(Operacion.HISTORIAL_BASICO, new NumeroReq(numero));
            if (!r.ok) throw new RuntimeException(r.error);
            return (List<Object[]>) r.data;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (historial)", e);
        }
    }

    @Override
    public Object[] ultimaTransaccionBasica(String numero) {
        try {
            Response r = call(Operacion.ULTIMA_TX_BASICA, new NumeroReq(numero));
            if (!r.ok) throw new RuntimeException(r.error);
            return (Object[]) r.data;
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (última tx)", e);
        }
    }
    
    @Override
    public void actualizarCliente(model.Cliente c) {
        try {
            var req = new ActualizarClienteReq(
                c.getIdCliente(), c.getNombre(), c.getApellido(),
                c.getDireccion(), c.getTelefono(), c.getEmail()
            );
            Response r = call(Operacion.ACTUALIZAR_CLIENTE, req);
            if (!r.ok) throw new RuntimeException(r.error);
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (actualizar cliente)", e);
        }
    }

    @Override
    public void actualizarCuenta(model.Cuenta cuenta) {
        try {
            var req = new ActualizarCuentaReq(cuenta.getNumCuenta(), cuenta.getPIN());
            Response r = call(Operacion.ACTUALIZAR_CUENTA, req);
            if (!r.ok) throw new RuntimeException(r.error);
        } catch (Exception e) {
            throw new RuntimeException("Error remoto (actualizar cuenta)", e);
        }
    }
}

