/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.impl;
import exception.FondosInsuficientesException;
import model.Cuenta;
import model.EnumTipo;
import model.Transaccion;
import java.util.Date;
/**
 *
 * @author XPC
 */
public class Retiro extends Transaccion {
    private Cuenta cuentaOrigen;
    public Retiro(Cuenta cuentaOrigen, double monto) {
        this.cuentaOrigen = cuentaOrigen;
        this.monto = monto;
        this.tipo = EnumTipo.RETIRO;
        this.fecha = new Date();
    }
    
    
    @Override
    public void ejecutar() throws FondosInsuficientesException { 
        if (cuentaOrigen.getBalance()< monto) {
            this.estado = "Fallida";
            throw new FondosInsuficientesException("Saldo insuficiente");
        }
        cuentaOrigen.retirar(monto);
        this.estado = "Exitosa";
        cuentaOrigen.getHistorialTransacciones().add(this);

    
    }
}
