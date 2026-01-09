import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IphoneCalculator extends JFrame implements ActionListener {

    // INSTANCE VARIABLES
    private JTextField display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    // CONSTRUCTOR
    public IphoneCalculator() {

        setTitle("Calculator");
        setSize(500, 812);                      // Size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Display
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 60));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        add(display, BorderLayout.NORTH);

        // Button Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 15, 15));   // Spacing
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttons = {
                "AC", "±", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "−",
                "1", "2", "3", "+",
                " ", "0", ".", "="
        };

        for (String text : buttons) {
            panel.add(createButton(text));
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // BUTTON CREATION
    private JButton createButton(String text) {

        // Circular button
        JButton button = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(getBackground());
                g.drawOval(0, 0, getWidth(), getHeight());
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {      // PRESS ANIMATION
         
            @Override
            public void mousePressed(MouseEvent e) {
                button.setFont(button.getFont().deriveFont(16f));  // shrink text
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setFont(button.getFont().deriveFont(40f));  // return to normal
            }
        });


        // Button colors
        if ("÷×−+=".contains(text)) {
            button.setBackground(new Color(255, 159, 10)); // orange
            button.setForeground(Color.WHITE);
        } else if ("AC±%".contains(text)) {
            button.setBackground(Color.LIGHT_GRAY);
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(new Color(60, 60, 60));
            button.setForeground(Color.WHITE);
        }

        return button;
    }

    // EVENT HANDLING
    @Override
    public void actionPerformed(ActionEvent e) {

        String input = e.getActionCommand();

        // Numbers
        if (input.matches("[0-9]")) {
            if (startNewNumber) {
                display.setText(input);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + input);
            }
        }

        // Decimal point
        else if (input.equals(".")) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        }

        // Clear
        else if (input.equals("AC")) {
            display.setText("0");
            firstNumber = 0;
            operator = "";
            startNewNumber = true;
        }

        // Plus / Minus
        else if (input.equals("±")) {
            double value = Double.parseDouble(display.getText());
            display.setText(formatNumber(value * -1));
        }

        // Percentage
        else if (input.equals("%")) {
            double value = Double.parseDouble(display.getText());
            display.setText(formatNumber(value / 100));
        }

        // Operators
        else if ("÷×−+".contains(input)) {
            firstNumber = Double.parseDouble(display.getText());
            operator = input;
            startNewNumber = true;
        }

        // Equals
        else if (input.equals("=")) {

            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;

            try {
                switch (operator) {
                    case "+":
                        result = firstNumber + secondNumber;
                        break;
                    case "−":
                        result = firstNumber - secondNumber;
                        break;
                    case "×":
                        result = firstNumber * secondNumber;
                        break;
                    case "÷":
                        if (secondNumber == 0) {
                            throw new ArithmeticException();
                        }
                        result = firstNumber / secondNumber;
                        break;
                }

                display.setText(formatNumber(result));

            } catch (ArithmeticException ex) {
                display.setText("Error");
            }

            startNewNumber = true;
        }
    }

    //FORMAT OUTPUT
    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            return String.valueOf(number);
        }
    }

    //MAIN METHOD
    public static void main(String[] args) {
        new IphoneCalculator();
    }
}