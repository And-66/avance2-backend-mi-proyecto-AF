/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;
/**
 *
 * @author XPC
 */
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idCliente;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    
    private static final Pattern EMAIL_PATTERN =Pattern.compile("^\\S+@\\S+\\.\\S+$");

    public Cliente(int idCliente, String nombre, String apellido, String direccion, String telefono, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
    
    
    public void registrarCliente() {
        validarCampos();
    }
    
    public void actualizarDatos() {
        validarCampos();
    }
    
    public String obtenerInformacion() { 
        return String.format(
            "Cliente ID: %d\nNombre: %s %s\nDirección: %s\nTeléfono: %s\nEmail: %s",
            idCliente, nombre, apellido, direccion, telefono, email
        );
    }
    
        private void validarCampos() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El campo del nombre es obligatorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El campo del apellido es obligatorio");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("El campo de dirección es obligatorio");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El campo de teléfono es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El campo de email es obligatorio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de email incorrecto");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;
        return idCliente == cliente.idCliente;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente);
    }

    @Override
    public String toString() {
        return String.format("%s %s (ID %d)", nombre, apellido, idCliente);
    }

}
