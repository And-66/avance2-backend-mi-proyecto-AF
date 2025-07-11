/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author XPC
 */
public class Cuenta implements Serializable {
    private String numCuenta;
    private double balance;
    private String PIN;
    private Cliente cliente;
    private List<model.Transaccion> historialTransacciones;

    public void depositar(double monto) { }
    public void retirar(double monto) { }
    public void transferir(double monto, Cuenta cuentaDestino) { }
    public boolean validarPIN(String pin) { return false; }
    public void imprimirComprobante(Transaccion transaccion) { }
    @Override
    public String toString() { return super.toString(); }
    public List<Transaccion> getHistorialTransacciones() { return null; }
}