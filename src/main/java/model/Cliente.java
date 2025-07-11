/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
/**
 *
 * @author XPC
 */
public class Cliente implements Serializable {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;

    public void registrarCliente() { }
    public void actualizarDatos() { }
    public String obtenerInformacion() { return null; }
}
