/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.impl;
import model.Cuenta;
import model.EnumTipo;
import model.Transaccion;
import java.util.Date;
/**
 *
 * @author XPC
 */
public class Deposito extends Transaccion {
    private Cuenta cuentaDestino;
    
    public Deposito(Cuenta cuentaDestino, double monto) { 
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.tipo = EnumTipo.DEPOSITO;
        this.fecha = new Date();    
    }
    
    
    @Override
    public void ejecutar() { 
        cuentaDestino.depositar(monto);
        this.estado = "Exitosa";
        registrar();    
    }
}
