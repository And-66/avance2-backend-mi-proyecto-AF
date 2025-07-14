/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import exception.AutenticacionException;
import exception.CuentaNoEncontradaException;

/**
 *
 * @author XPC
 */
public class CajeroAutomatico {
    private String idCajero;
    private String ubicacion;
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;
    private Banco banco;
    private static int contadorRecibos = 1;
    
    public CajeroAutomatico(String idCajero, String ubicacion, Banco banco) {
        this.idCajero  = idCajero;
        this.ubicacion = ubicacion;
        this.banco     = banco;
    }

    public String getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(String idCajero) {
        this.idCajero = idCajero;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public static int getContadorRecibos() {
        return contadorRecibos;
    }

    public static void setContadorRecibos(int contadorRecibos) {
        CajeroAutomatico.contadorRecibos = contadorRecibos;
    }
    
    
    
    public void autenticarCliente(String numCuenta, String PIN)
            throws CuentaNoEncontradaException, AutenticacionException {
        Cuenta cuenta = banco.buscarCuenta(numCuenta);
        if (!cuenta.validarPIN(PIN)) {
            intentosFallidos++;
            if (intentosFallidos >= MAX_INTENTOS) {
                throw new AutenticacionException(
                    "Cuenta bloqueada tras " + MAX_INTENTOS + " intentos");
            }
            throw new AutenticacionException("PIN incorrecto");
        }
        intentosFallidos = 0;
    }
    
    
    public void mostrarMenuOperaciones() {
        System.out.println("----- Menú de Operaciones -----");
        System.out.println("1. Retiro");
        System.out.println("2. Depósito");
        System.out.println("3. Transferencia");
        System.out.println("4. Historial");
        System.out.println("5. Actualizar Datos");
        System.out.println("6. Cerrar Sesión");
        System.out.println("-------------------------------");
    }
    
    
    public void ejecutarTransaccion(Transaccion transaccion) throws Exception {
        transaccion.ejecutar();
    }
    
    
    public void imprimirComprobante(Transaccion transaccion) {
        Comprobante comprobante = new Comprobante(contadorRecibos++);
        comprobante.generar(transaccion);
        comprobante.imprimir();
    }
    
}
