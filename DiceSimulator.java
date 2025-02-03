import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class DiceSimulator extends JFrame {
    private JButton rollButton;
    private DicePanel dicePanel;

    public DiceSimulator() {
        // Set up the frame
        setTitle("Dice Simulator");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create a panel to draw the dice
        dicePanel = new DicePanel();
        add(dicePanel, BorderLayout.CENTER);

        // Create a button to roll the dice
        rollButton = new JButton("Roll the Dice");
        add(rollButton, BorderLayout.SOUTH);

        // Add an action listener to the button
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dicePanel.rollDice();
            }
        });

        // Make the window visible
        setVisible(true);
    }

    // DicePanel to handle drawing the dice face
    class DicePanel extends JPanel {
        private int result;

        public DicePanel() {
            rollDice();  // Initially roll the dice when the app starts
        }

        // Method to simulate a dice roll
        public void rollDice() {
            Random random = new Random();
            result = random.nextInt(6) + 1; // Random number between 1 and 6
            repaint(); // Repaint the panel to show the new dice result
        }

        // Custom paintComponent method to draw the dice
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the square for the dice face
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 200, 200); // Dice face size is 200x200
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 200, 200); // Border of the dice face

            // Draw the dots based on the result
            drawDots(g, result);
        }

        // Method to draw the dots based on the dice result
        private void drawDots(Graphics g, int number) {
            int dotSize = 40; // Size of each dot
            int[] xCoords = {100, 50, 150, 50, 150, 50, 150};
            int[] yCoords = {100, 50, 50, 150, 150, 100, 100};

            // Adjust the dot positions for the dice
            switch (number) {
                case 1:
                    g.fillOval(100 - dotSize / 2, 100 - dotSize / 2, dotSize, dotSize); // Center dot
                    break;
                case 2:
                    g.fillOval(50 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-left
                    g.fillOval(150 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-right
                    break;
                case 3:
                    g.fillOval(50 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-left
                    g.fillOval(100 - dotSize / 2, 100 - dotSize / 2, dotSize, dotSize); // Center
                    g.fillOval(150 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-right
                    break;
                case 4:
                    g.fillOval(50 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-left
                    g.fillOval(150 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-right
                    g.fillOval(50 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-left
                    g.fillOval(150 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-right
                    break;
                case 5:
                    g.fillOval(50 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-left
                    g.fillOval(150 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-right
                    g.fillOval(100 - dotSize / 2, 100 - dotSize / 2, dotSize, dotSize); // Center
                    g.fillOval(50 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-left
                    g.fillOval(150 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-right
                    break;
                case 6:
                    g.fillOval(50 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-left
                    g.fillOval(150 - dotSize / 2, 50 - dotSize / 2, dotSize, dotSize); // Top-right
                    g.fillOval(50 - dotSize / 2, 100 - dotSize / 2, dotSize, dotSize); // Middle-left
                    g.fillOval(150 - dotSize / 2, 100 - dotSize / 2, dotSize, dotSize); // Middle-right
                    g.fillOval(50 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-left
                    g.fillOval(150 - dotSize / 2, 150 - dotSize / 2, dotSize, dotSize); // Bottom-right
                    break;
            }
        }
    }

    public static void main(String[] args) {
        // Start the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DiceSimulator();
            }
        });
    }
}
