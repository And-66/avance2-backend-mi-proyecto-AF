/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.impl;
import model.Cuenta;
import model.Transaccion;
/**
 *
 * @author XPC
 */
public class Transferencia extends Transaccion {
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    public Transferencia(Cuenta origen, Cuenta destino, double monto) { }
    @Override
    public void ejecutar() { }
}
