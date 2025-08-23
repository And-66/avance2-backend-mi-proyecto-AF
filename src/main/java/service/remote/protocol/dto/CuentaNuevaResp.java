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
public class CuentaNuevaResp implements Serializable {
    private static final long serialVersionUID = 1L;
    public String numero;
    public BigDecimal saldo;
    public CuentaNuevaResp(String numero, BigDecimal saldo) { this.numero = numero; this.saldo = saldo; }
}

