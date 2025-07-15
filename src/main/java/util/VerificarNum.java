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
        String text = ((JTextField) input).getText().trim();
        if (text.isEmpty()) {
            return true;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = verify(input);
        String text = ((JTextField) input).getText().trim();
        if (!text.isEmpty() && !valid) {
            JOptionPane.showMessageDialog(
                input,
                "Ingrese un número válido",
                "Validación Numérica",
                JOptionPane.WARNING_MESSAGE
            );
        }
        return true;
    }
 
}
