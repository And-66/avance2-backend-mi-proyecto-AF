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
public class Transferencia extends Transaccion {
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    
    public Transferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.tipo = EnumTipo.TRANSFERENCIA;
        this.fecha = new Date();
    }
    
    
    @Override
    public void ejecutar() throws FondosInsuficientesException {
        cuentaOrigen.transferir(monto, cuentaDestino);
        this.estado = "Exitosa";
        registrar();   
    }
}
