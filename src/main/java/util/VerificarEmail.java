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
public class VerificarEmail extends InputVerifier {
        @Override
    public boolean verify(JComponent input) {
        String email = ((JTextField) input).getText();
        return email.matches("^\\S+@\\S+\\.\\S+$");
    }
    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean ok = verify(input);
        if (!ok) JOptionPane.showMessageDialog(input, "Formato de email incorrecto");
        return ok;
    }    
}
