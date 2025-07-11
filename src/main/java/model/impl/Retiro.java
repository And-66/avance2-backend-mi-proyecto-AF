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
public class Retiro extends Transaccion {
    private Cuenta cuentaOrigen;
    public Retiro(Cuenta cuenta, double monto) { }
    @Override
    public void ejecutar() { }
}
