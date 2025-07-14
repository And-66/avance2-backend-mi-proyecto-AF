/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import javax.swing.*;

/**
 *
 * @author XPC
 */
public class VerificarNum extends InputVerifier {
        @Override
    public boolean verify(JComponent input) {
        try {
            Double.parseDouble(((JTextField) input).getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean ok = verify(input);
        if (!ok) JOptionPane.showMessageDialog(input, "Valor numérico inválido");
        return ok;
    }    
}
