/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.util.regex.Pattern;
import javax.swing.*;

/**
 *
 * @author XPC
 */
public class VerificarEmail extends InputVerifier {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^\\S+@\\S+\\.\\S+$");

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        if (text.isEmpty()) {
            return true;
        }
        return EMAIL_PATTERN.matcher(text).matches();
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = verify(input);
        String text = ((JTextField) input).getText().trim();
        if (!text.isEmpty() && !valid) {
            JOptionPane.showMessageDialog(
                input,
                "Formato de email inválido (ej: usuario@dominio.com)",
                "Validación Email",
                JOptionPane.WARNING_MESSAGE
            );
        }
        return true;
    }
   
}
