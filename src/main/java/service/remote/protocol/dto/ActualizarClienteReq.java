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
public class ActualizarClienteReq implements Serializable {
    private static final long serialVersionUID = 1L;
    public int idCliente;
    public String nombre, apellido, direccion, telefono, email;
    public ActualizarClienteReq(int idCliente, String nombre, String apellido,
                                String direccion, String telefono, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre; this.apellido = apellido;
        this.direccion = direccion; this.telefono = telefono; this.email = email;
    }
}
