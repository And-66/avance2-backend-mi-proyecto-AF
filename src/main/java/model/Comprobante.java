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
public class Comprobante implements Serializable {
    private int idRecibo;
    private Date fechaEmision;
    private String detallesTransaccion;

    public void generar() { }
    public void imprimir() { }
}
