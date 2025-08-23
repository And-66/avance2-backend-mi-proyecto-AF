/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.remote.protocol.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author XPC
 */
public class MontoReq implements Serializable {
    private static final long serialVersionUID = 1L;
    public String numero;
    public BigDecimal monto;
    public MontoReq(String numero, BigDecimal monto) { this.numero = numero; this.monto = monto; }
}

