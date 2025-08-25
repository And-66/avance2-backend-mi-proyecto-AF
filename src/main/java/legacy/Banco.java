/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package legacy;

import exception.CuentaNoEncontradaException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Cliente;
import model.Cuenta;
/**
 *
 * @author XPC
 */
@Deprecated
public class Banco implements Serializable {
    private List<Cliente> listaClientes = new ArrayList<>();
    private List<Cuenta> listaCuentas  = new ArrayList<>();


    public void abrirCuenta(Cliente cliente, Cuenta cuenta) {
        listaClientes.add(cliente);
        listaCuentas.add(cuenta);
    }

    public Cuenta buscarCuenta(String numCuenta) throws CuentaNoEncontradaException {
        return listaCuentas.stream().filter(c -> c.getNumCuenta().equals(numCuenta)).findFirst().orElseThrow(() -> 
            new CuentaNoEncontradaException("Cuenta " + numCuenta + " no encontrada"));
    }

    public Cliente buscarCliente(int idCliente) {
        return listaClientes.stream().filter(c -> c.getIdCliente() == idCliente).findFirst().orElse(null);
    }

    public void actualizarCliente(Cliente clienteActualizado) {
        listaClientes.replaceAll(c -> c.getIdCliente() == clienteActualizado.getIdCliente() ? clienteActualizado : c);
    }
    
    public void actualizarCuenta(Cuenta cuentaActual) throws CuentaNoEncontradaException {
        for (int i = 0; i < listaCuentas.size(); i++) {
            if (listaCuentas.get(i).getNumCuenta().equals(cuentaActual.getNumCuenta())) {
                listaCuentas.set(i, cuentaActual);
                return;
            }
        }
        throw new CuentaNoEncontradaException(
            "No existe la cuenta: " + cuentaActual.getNumCuenta()
        );
    }
    
    
    public List<Cuenta> obtenerTodasCuentas() {
        return Collections.unmodifiableList(listaCuentas);
    }

    public List<Cliente> getListaClientes() {
        return Collections.unmodifiableList(listaClientes);
    }

    public List<Cuenta> getListaCuentas() {
        return Collections.unmodifiableList(listaCuentas);
    }
    
}
