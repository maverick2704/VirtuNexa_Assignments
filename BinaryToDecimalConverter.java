import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BinaryToDecimalConverter {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Binary to Decimal Converter");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        
        JLabel instructionLabel = new JLabel("Enter a binary number:");
        instructionLabel.setBounds(30, 20, 200, 30);
        frame.add(instructionLabel);

        
        JTextField binaryInput = new JTextField();
        binaryInput.setBounds(200, 20, 150, 30);
        frame.add(binaryInput);

        
        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(150, 70, 100, 30);
        frame.add(convertButton);

        
        JLabel resultLabel = new JLabel("Decimal: ");
        resultLabel.setBounds(30, 120, 300, 30);
        frame.add(resultLabel);

        
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String binary = binaryInput.getText();
                try {
                    
                    int decimal = Integer.parseInt(binary, 2);
                    resultLabel.setText("Decimal: " + decimal);
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Error: Invalid binary number!");
                }
            }
        });

        
        frame.setVisible(true);
    }
}
