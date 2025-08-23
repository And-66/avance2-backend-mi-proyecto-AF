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
public class LoginResp implements Serializable {
    private static final long serialVersionUID = 1L;
    public long clienteId;
    public String numero;
    public String nombre, apellido, direccion, telefono, email;
    public BigDecimal saldo;

    public LoginResp(long clienteId, String numero, String nombre, String apellido,
                     String direccion, String telefono, String email, BigDecimal saldo) {
        this.clienteId = clienteId; this.numero = numero; this.nombre = nombre; this.apellido = apellido;
        this.direccion = direccion; this.telefono = telefono; this.email = email; this.saldo = saldo;
    }
}

