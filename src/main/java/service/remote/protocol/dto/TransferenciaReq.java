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
public class TransferenciaReq implements Serializable {
    private static final long serialVersionUID = 1L;
    public String origen, destino;
    public BigDecimal monto;
    public TransferenciaReq(String origen, String destino, BigDecimal monto) {
        this.origen = origen; this.destino = destino; this.monto = monto;
    }
}

