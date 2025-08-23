/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.remote.protocol.dto;

import java.io.Serializable;

/**
 *
 * @author XPC
 */
public class AbrirCuentaReq implements Serializable {
    private static final long serialVersionUID = 1L;
    public String nombre, apellido, direccion, telefono, email, pin;
    public AbrirCuentaReq(String nombre, String apellido, String direccion,
                          String telefono, String email, String pin) {
        this.nombre = nombre; this.apellido = apellido; this.direccion = direccion;
        this.telefono = telefono; this.email = email; this.pin = pin;
    }
}

