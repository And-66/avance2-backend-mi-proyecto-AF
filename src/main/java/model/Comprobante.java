/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 *
 * @author XPC
 */
public class Comprobante implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idRecibo;
    private Date fechaEmision;
    private String detallesTransaccion;
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
   
    
    public String generar(Transaccion tx) {
        return generar(tx, null);
    }

    public String generar(Transaccion tx, String numeroCuenta) {
        if (tx == null) {
            return banner()
                + "Transacción: N/D\n"
                + "Detalle:     Transacción no disponible\n"
                + footer();
        }

        Date fecha   = (tx.getFecha() != null) ? tx.getFecha() : new Date();
        String cuenta = (numeroCuenta != null && !numeroCuenta.isBlank()) ? numeroCuenta : "N/D";
        String tipo   = (tx.getTipo() != null) ? tx.getTipo().name() : "N/D";
        String estado = (tx.getEstado() != null) ? tx.getEstado() : "N/D";

        return banner()
            + "Comprobante: " + id + "\n"
            + "Transacción: " + safeId(tx.getIdTransaccion()) + "\n"
            + "Fecha:       " + formatFecha(fecha) + "\n"
            + "Cuenta:      " + cuenta + "\n"
            + "Tipo:        " + tipo + "\n"
            + "Monto:       " + formatCurrency(tx.getMonto()) + "\n"
            + "Estado:      " + estado + "\n"
            + footer();
    }

    public String generar(Integer idTransaccion, Date fecha, String numeroCuenta,
                          String tipo, double monto, String estado) {
        return banner()
            + "Comprobante: " + id + "\n"
            + "Transacción: " + safeId(idTransaccion) + "\n"
            + "Fecha:       " + formatFecha(fecha != null ? fecha : new Date()) + "\n"
            + "Cuenta:      " + (numeroCuenta != null ? numeroCuenta : "N/D") + "\n"
            + "Tipo:        " + (tipo != null ? tipo : "N/D") + "\n"
            + "Monto:       " + formatCurrency(monto) + "\n"
            + "Estado:      " + (estado != null ? estado : "N/D") + "\n"
            + footer();
    }

    // ---------- helpers de formato ----------

    private String banner() { return "********* FideBank - Comprobante *********\n"; }
    private String footer() { return "*******************************************\n"; }

    private String formatFecha(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    private String formatCurrency(double amount) {
        return "₡" + String.format(Locale.US, "%,.2f", amount);
    }
    private String safeId(Integer idTx) { return (idTx == null) ? "N/D" : String.valueOf(idTx); }
    
    
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
