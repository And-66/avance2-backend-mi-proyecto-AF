/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.util.Date;
/**
 *
 * @author XPC
 */
public abstract class Transaccion implements Serializable {
    protected int idTransaccion;
    protected EnumTipo tipo;
    protected double monto;
    protected Date fecha;
    protected String estado;

    public abstract void ejecutar();
    protected void registrar() { }
    public String obtenerDetalleTransaccion() { return null; }
}
