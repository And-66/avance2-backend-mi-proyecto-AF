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
public class VerificarPIN extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        return text.matches("\\d{4}");
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = verify(input);
        String text = ((JTextField) input).getText();
        if (!valid && !text.isBlank()) {
            JOptionPane.showMessageDialog(
                input,
                "PIN debe tener exactamente 4 dígitos numéricos",
                "Validación PIN",
                JOptionPane.WARNING_MESSAGE
            );
        }
        return true;
    }

}
