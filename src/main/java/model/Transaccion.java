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
    public String estado;
    
    
    public abstract void ejecutar()throws Exception;
    protected void registrar() { 
    // Pendiente
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public EnumTipo getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipo tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String obtenerDetalleTransaccion() { 
        return String.format(
            "ID: %d | Tipo: %s | Monto: %.2f | Fecha: %s | Estado: %s",
            idTransaccion, tipo, monto, fecha, estado);
 
    }
}
