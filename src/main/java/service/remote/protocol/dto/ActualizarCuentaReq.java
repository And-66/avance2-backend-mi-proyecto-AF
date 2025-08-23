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
public class ActualizarCuentaReq implements Serializable {
    private static final long serialVersionUID = 1L;
    public String numero;
    public String pin; // nuevo PIN en claro (el server lo hashea)
    public ActualizarCuentaReq(String numero, String pin) {
        this.numero = numero; this.pin = pin;
    }
}

