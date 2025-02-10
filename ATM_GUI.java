import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ATM_GUI {
    private JFrame frame;
    private JPanel mainPanel, loginPanel;
    private JButton btnWithdraw, btnCheckBalance, btnDeposit, btnBankTransfer, btnPayBills, btnTransactionHistory;
    private JButton btnLogin;
    private JTextArea textArea;
    private JPasswordField pinField;
    private double balance = 1000.00; // Initial balance
    private String correctPin = "1234"; // Example PIN
    private Image loginBackgroundImage, mainBackgroundImage;
    private ArrayList<String> transactionHistory = new ArrayList<>(); // Store transaction history

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ATM_GUI window = new ATM_GUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ATM_GUI() {
        // Load the background images
        loginBackgroundImage = new ImageIcon("src/1.jpg").getImage(); // Background for login
        mainBackgroundImage = new ImageIcon("src/2.jpg").getImage(); // Background for main panel

        // Initialize the frame
        frame = new JFrame("ATM Machine");
        frame.setBounds(200, 200, 600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Login Panel
        loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(loginBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblPin = new JLabel("Enter PIN:");
        lblPin.setForeground(Color.WHITE); // Set text color for readability
        lblPin.setFont(new Font("Arial", Font.BOLD, 16)); // Custom font
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(lblPin, gbc);

        pinField = new JPasswordField(10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(pinField, gbc);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(btnLogin, gbc);

        frame.getContentPane().add(loginPanel, BorderLayout.CENTER);

        // Main Panel
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mainBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);

        btnWithdraw = createStyledButton("Withdraw");
        btnWithdraw.addActionListener(e -> requestPinForTransaction("Withdraw"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(btnWithdraw, gbc);

        btnCheckBalance = createStyledButton("Check Balance");
        btnCheckBalance.addActionListener(e -> requestPinForTransaction("Check Balance"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(btnCheckBalance, gbc);

        btnDeposit = createStyledButton("Deposit");
        btnDeposit.addActionListener(e -> requestPinForTransaction("Deposit"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(btnDeposit, gbc);

        btnBankTransfer = createStyledButton("Bank Transfer");
        btnBankTransfer.addActionListener(e -> requestPinForTransaction("Bank Transfer"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnBankTransfer, gbc);

        btnPayBills = createStyledButton("Pay Bills");
        btnPayBills.addActionListener(e -> requestPinForTransaction("Pay Bills"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(btnPayBills, gbc);

        btnTransactionHistory = createStyledButton("Transaction History");
        btnTransactionHistory.addActionListener(e -> showTransactionHistory());
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(btnTransactionHistory, gbc);

        // Text Area for displaying account information
        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.SOUTH);
    }

    // Create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.CYAN);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    // Handle Login
    private void handleLogin() {
        char[] enteredPin = pinField.getPassword();
        String enteredPinStr = new String(enteredPin);

        if (enteredPinStr.equals(correctPin)) {
            loginPanel.setVisible(false);
            frame.getContentPane().remove(loginPanel);
            frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            checkBalance();
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect PIN. Try again.");
        }
    }

    // Request the PIN for each transaction
    private void requestPinForTransaction(String transactionType) {
        JPasswordField transactionPinField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(frame, transactionPinField,
                "Enter PIN to " + transactionType, JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String enteredPinStr = new String(transactionPinField.getPassword());
            if (enteredPinStr.equals(correctPin)) {
                switch (transactionType) {
                    case "Withdraw":
                        handleWithdraw();
                        break;
                    case "Check Balance":
                        checkBalance();
                        break;
                    case "Deposit":
                        handleDeposit();
                        break;
                    case "Bank Transfer":
                        JOptionPane.showMessageDialog(frame, "Feature coming soon!");
                        break;
                    case "Pay Bills":
                        JOptionPane.showMessageDialog(frame, "Feature coming soon!");
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect PIN. Try again.");
            }
        }
    }

    // Handle Withdraw operation
    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                } else {
                    balance -= amount;
                    recordTransaction("Withdraw: PHP " + amount);
                    JOptionPane.showMessageDialog(frame, "Withdrawal successful!");
                    checkBalance();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
            }
        }
    }

    // Handle Deposit operation
    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                balance += amount;
                recordTransaction("Deposit: PHP " + amount);
                JOptionPane.showMessageDialog(frame, "Deposit successful!");
                checkBalance();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
            }
        }
    }

    // Check the balance
    private void checkBalance() {
        textArea.setText("Current Balance: PHP " + balance);
    }

    // Record the transaction
    private void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    // Show Transaction History
    private void showTransactionHistory() {
        StringBuilder history = new StringBuilder();
        for (String transaction : transactionHistory) {
            history.append(transaction).append("\n");
        }
        textArea.setText(history.toString());
    }
}
