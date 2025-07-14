/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author XPC
 */
public class Comprobante implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idRecibo;
    private Date fechaEmision;
    private String detallesTransaccion;
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public Comprobante(int idRecibo) {
        this.idRecibo = idRecibo;
    }

    public int getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(int idRecibo) {
        this.idRecibo = idRecibo;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDetallesTransaccion() {
        return detallesTransaccion;
    }

    public void setDetallesTransaccion(String detallesTransaccion) {
        this.detallesTransaccion = detallesTransaccion;
    }
   
    
    public void generar(Transaccion transaccion) {
         this.fechaEmision = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("----- COMPROBANTE FideBank -----\n");
        sb.append("Recibo #: ").append(idRecibo).append("\n");
        sb.append("Fecha:      ").append(FORMATO_FECHA.format(fechaEmision)).append("\n\n");
        sb.append(transaccion.obtenerDetalleTransaccion()).append("\n");
        sb.append("-------------------------------\n");
        this.detallesTransaccion = sb.toString();
    }
    public void imprimir() {
        if (detallesTransaccion == null) {
            throw new IllegalStateException("Debe generar el comprobante antes de imprimirlo.");
        }
        System.out.println(detallesTransaccion);
    }
    
    @Override
    public String toString() {
        return detallesTransaccion != null
            ? detallesTransaccion
            : String.format(
                "Comprobante[id=%d, fecha=%s]",
                idRecibo,
                fechaEmision == null ? "N/A" : FORMATO_FECHA.format(fechaEmision)
              );
    }
    
    
}
