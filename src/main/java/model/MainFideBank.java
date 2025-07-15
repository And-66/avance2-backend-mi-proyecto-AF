/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package model;

import service.BancoService;
import ui.LoginPanel;

/**
 *
 * @author XPC
 */
public class MainFideBank {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BancoService bs = new BancoService();
        Runtime.getRuntime().addShutdownHook(new Thread(bs::guardar));
        java.awt.EventQueue.invokeLater(() -> {
            new LoginPanel(bs).setVisible(true);
        });
    }

  
}
