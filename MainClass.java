import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class MainClass {
    public static void main(String[] args) {
        MainClass mc = new MainClass();
        mc.launch();
    }

    JFrame frame;
    JPanel panel, mainPanel;
    JTextArea display;
    JButton cleanButton, backspaceButton, plusMinusButton, pi;
    String command;
    boolean commandPressed, appendDigits;
    BigDecimal bigNumber, result;

    private void launch() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        frame = new JFrame("Калькулятор");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 250, 320);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5,5));

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        display = new JTextArea();
        display.setEditable(false);
        display.setFont(new Font("Serif", Font.PLAIN, 18));
        display.setLineWrap(true);
        display.setBorder(BorderFactory.createLineBorder(Color.GRAY));


        cleanButton = new JButton("С");
        cleanButton.setFont(new Font("Serif", Font.PLAIN, 16));
        cleanButton.addActionListener(new ClearListener());

        backspaceButton = new JButton("←");
        backspaceButton.setFont(new Font("Serif", Font.PLAIN, 16));
        backspaceButton.addActionListener(new BackspaceListener());

        plusMinusButton = new JButton("+/-");
        plusMinusButton.addActionListener(new PlusMinusListener());

        pi = new JButton("π");
        pi.addActionListener(new PiListener());
        pi.setFont(new Font("Serif", Font.PLAIN, 16));

        panel.add(cleanButton);
        panel.add(backspaceButton);
        panel.add(plusMinusButton);
        panel.add(pi);

        makeButton("7", new DigitListener());
        makeButton("8", new DigitListener());
        makeButton("9", new DigitListener());
        makeButton("/", new CommandListener());

        makeButton("4", new DigitListener());
        makeButton("5", new DigitListener());
        makeButton("6", new DigitListener());
        makeButton("*", new CommandListener());

        makeButton("1", new DigitListener());
        makeButton("2", new DigitListener());
        makeButton("3", new DigitListener());
        makeButton("-", new CommandListener());

        makeButton("0", new DigitListener());
        makeButton(".", new DigitListener());
        makeButton("=", new CommandListener());
        makeButton("+", new CommandListener());

        mainPanel.add(display, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        frame.setMinimumSize(new Dimension(245, 215));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        commandPressed = false;
        appendDigits = false;
        result = new BigDecimal("0");
    }

    private void makeButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setFont(new Font("Serif", Font.PLAIN, 16));
        panel.add(button);
    }

    private void calculate(String number) {
        bigNumber = new BigDecimal(number);
        switch (command) {
            case "+": result = result.add(bigNumber);
                break;
            case "-": result = result.subtract(bigNumber);
                break;
            case "*": result = result.multiply(bigNumber);
                break;
            case "/": result = result.divide(bigNumber, 10, BigDecimal.ROUND_HALF_UP);
                break;
            default: result = new BigDecimal(number);
                break;
        }
        display.setText("" + result.stripTrailingZeros());

        if (command.equals("=")) {
            commandPressed = false;
            appendDigits = false;
            command = null;
        }
    }

    public class CommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (commandPressed) {
                calculate(display.getText());
                command = e.getActionCommand();
            } else {
                result = new BigDecimal(display.getText());
                command = e.getActionCommand();
            }
            commandPressed = true;
            appendDigits = false;
        }
    }

    public class DigitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (appendDigits) {
                display.append(e.getActionCommand());
            } else {
                display.setText(e.getActionCommand());
                appendDigits = true;
            }
        }
    }

    public class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            display.setText("");
            commandPressed = false;
            appendDigits = false;
            result = result.subtract(result.negate());
        }
    }

    public class BackspaceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = display.getText();
            if (text.length() > 0)
                display.setText(text.substring(0, text.length()-1));
        }
    }

    public class PlusMinusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = display.getText();
            if (!text.contains("-"))
                display.setText("-" + text);
            else
                display.setText(text.substring(1));
        }
    }

    public class PiListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            display.setText("3.14159265359");
        }
    }
}
