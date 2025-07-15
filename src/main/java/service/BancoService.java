/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Banco;
import java.io.IOException;


/**
 *
 * @author XPC
 */
public class BancoService {
    private static final String RUTA_DB = "banco.dat";
    private Banco banco;

    public BancoService() {
        try {
            banco = (Banco) Serializacion.cargar(RUTA_DB);
        } catch (Exception e) {
            banco = new Banco();
        }
    }

    public Banco getBanco() {
        return banco;
    }

    public void guardar() {
        try {
            Serializacion.guardar(banco, RUTA_DB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
