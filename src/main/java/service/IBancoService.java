/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.AutenticacionException;
import exception.FondosInsuficientesException;
import model.Cliente;
import model.Cuenta;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author XPC
 */
public interface IBancoService {
    Cuenta loginPorPin(String numCuenta, String pin) throws AutenticacionException;
    Cuenta abrirCuenta(Cliente cli, String pin);
    BigDecimal depositar(String numero, BigDecimal monto);
    BigDecimal retirar(String numero, BigDecimal monto) throws FondosInsuficientesException;
    BigDecimal transferir(String origen, String destino, BigDecimal monto) throws FondosInsuficientesException;
    BigDecimal consultarSaldo(String numero);
    List<Object[]> listarHistorialBasico(String numero);
    Object[] ultimaTransaccionBasica(String numero);
    void actualizarCliente(model.Cliente cliente);
    void actualizarCuenta(model.Cuenta cuenta);
}
