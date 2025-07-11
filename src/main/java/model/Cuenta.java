/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import exception.FondosInsuficientesException;
/**
 *
 * @author XPC
 */
public class Cuenta implements Serializable {
    private String numCuenta;
    private double balance;
    private String PIN;
    private Cliente cliente;
    private List<Transaccion> historialTransacciones = new ArrayList<>();

    public Cuenta(String numCuenta, Cliente cliente, String PIN, double saldoInicial) {
        this.numCuenta = numCuenta;
        this.cliente = cliente;
        this.PIN = PIN;
        this.balance = saldoInicial;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
   
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor que cero");
        }
        balance += monto;
    }
    
    public void retirar(double monto) throws FondosInsuficientesException { 
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor que cero");
        }
        if (monto > balance) {
            throw new FondosInsuficientesException("Saldo insuficiente");
        }
        balance -= monto;   
    }
    
    public void transferir(double monto, Cuenta cuentaDestino) throws FondosInsuficientesException {
        if (cuentaDestino == null) {
            throw new IllegalArgumentException("Cuenta destino inválida");
        }
        this.retirar(monto);
        cuentaDestino.depositar(monto);              
    }
    
    public boolean validarPIN(String pin) { 
        return PIN.equals(pin); 
    }
    
    public void imprimirComprobante(Transaccion transaccion) {
        // llama a Comprobante.generar() e imprimir()         
    }
    @Override
    public String toString() { 
        return String.format(
            "Cuenta[%s] - Saldo: %.2f - Titular: %s %s",
            numCuenta, balance, cliente.getNombre(), cliente.getApellido());

    }
    
    public List<Transaccion> getHistorialTransacciones() { 
        return historialTransacciones; 
    }    
}