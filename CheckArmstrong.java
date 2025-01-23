import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckArmstrong {
    public static void main(String[] args) {
        // Create a frame
        JFrame frame = new JFrame("Armstrong Number Checker");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create components
        JLabel label = new JLabel("Enter a number:");
        JTextField textField = new JTextField();
        JButton checkButton = new JButton("Check");
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);

        // Add components to the panel
        panel.add(label);
        panel.add(textField);
        panel.add(checkButton);

        // Add panel and result label to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(resultLabel, BorderLayout.SOUTH);

        // Add action listener to the button
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get input number from the text field
                    int number = Integer.parseInt(textField.getText());
                    // Check if it's an Armstrong number
                    if (isArmstrong(number)) {
                        resultLabel.setText(number + " is an Armstrong number.");
                    } else {
                        resultLabel.setText(number + " is not an Armstrong number.");
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Please enter a valid number.");
                }
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }

    // Method to check if a number is an Armstrong number
    public static boolean isArmstrong(int num) {
        int originalNumber = num;
        int sum = 0;
        int digits = String.valueOf(num).length();

        while (num != 0) {
            int digit = num % 10;
            sum += Math.pow(digit, digits);
            num /= 10;
        }

        return sum == originalNumber;
    }
}
