import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ATM1_GUI {
    private JFrame frame;
    private JPanel mainPanel, transferPanel, loginPanel, transactionPanel, payBillsPanel, historyPanel;
    private JButton btnWithdraw, btnCheckBalance, btnDeposit, btnBankTransfer, btnPayBills, btnTransactionHistory;
    private JButton btnLogin;
    private JTextArea textArea;
    private JPasswordField pinField;
    private double balance = 1000.00; // initial balance
    private String correctPin = "1234"; // Example PIN
    private Image loginBackgroundImage, mainBackgroundImage;
    private Image backgroundImage;
    private ArrayList<String> transactionHistory = new ArrayList<>(); // Store transaction history

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ATM1_GUI window = new ATM1_GUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ATM1_GUI() {
    
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
        gbc.insets = new Insets(5, 5, 5, 5); // Padding for components

        JLabel lblPin = new JLabel("Enter PIN:");
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

        // Main panel with buttons
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mainBackgroundImage, 0, 0, getWidth(), getHeight(), this); // Use mainBackgroundImage
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
       
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for buttons

        btnWithdraw = new JButton("Withdraw");
        btnWithdraw.addActionListener(e -> requestPinForTransaction("Withdraw"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(btnWithdraw, gbc);

        btnCheckBalance = new JButton("Check Balance");
        btnCheckBalance.addActionListener(e -> requestPinForTransaction("Check Balance"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(btnCheckBalance, gbc);

        btnDeposit = new JButton("Deposit");
        btnDeposit.addActionListener(e -> requestPinForTransaction("Deposit"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(btnDeposit, gbc);

        btnBankTransfer = new JButton("Bank Transfer");
        btnBankTransfer.addActionListener(e -> requestPinForTransaction("Bank Transfer"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnBankTransfer, gbc);

        btnPayBills = new JButton("Pay Bills");
        btnPayBills.addActionListener(e -> requestPinForTransaction("Pay Bills"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(btnPayBills, gbc);

        btnTransactionHistory = new JButton("Transaction History");
        btnTransactionHistory.addActionListener(e -> showTransactionHistory());
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(btnTransactionHistory, gbc);

        // Text Area for displaying account information
        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.SOUTH);

        // Panel for bank transfer options
        transferPanel = new JPanel();
        transferPanel.setLayout(new GridLayout(6, 1));

        JButton btnBDO = new JButton("BDO");
        btnBDO.addActionListener(e -> processTransfer("BDO"));
        transferPanel.add(btnBDO);

        JButton btnUnionBank = new JButton("Union Bank");
        btnUnionBank.addActionListener(e -> processTransfer("Union Bank"));
        transferPanel.add(btnUnionBank);

        JButton btnBPI = new JButton("BPI");
        btnBPI.addActionListener(e -> processTransfer("BPI"));
        transferPanel.add(btnBPI);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> showMainPanel());
        transferPanel.add(btnBack);

        // Panel for paying bills
        payBillsPanel = new JPanel();
        payBillsPanel.setLayout(new GridLayout(6, 1));

        JButton btnMaynilad = new JButton("Maynilad");
        btnMaynilad.addActionListener(e -> processBillPayment("Maynilad"));
        payBillsPanel.add(btnMaynilad);

        JButton btnMeralco = new JButton("Meralco");
        btnMeralco.addActionListener(e -> processBillPayment("Meralco"));
        payBillsPanel.add(btnMeralco);

        JButton btnPLDT = new JButton("PLDT");
        btnPLDT.addActionListener(e -> processBillPayment("PLDT"));
        payBillsPanel.add(btnPLDT);


        JButton btnBackBills = new JButton("Back");
        btnBackBills.addActionListener(e -> showMainPanel());
        payBillsPanel.add(btnBackBills);

        // Panel for transaction history
        historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());

        JButton btnBackHistory = new JButton("Back");
        btnBackHistory.addActionListener(e -> showMainPanel());
        historyPanel.add(btnBackHistory, BorderLayout.SOUTH);
    }

    // Handle Login
    private void handleLogin() {
        char[] enteredPin = pinField.getPassword();
        String enteredPinStr = new String(enteredPin);

        if (enteredPinStr.equals(correctPin)) {
            // Show the main panel after successful login
            loginPanel.setVisible(false);
            frame.getContentPane().remove(loginPanel);
            frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            checkBalance(); // Display the balance when logged in
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect PIN. Try again.");
        }
    }

    // Request the PIN for each transaction
    private void requestPinForTransaction(String transactionType) {
        // Ask for the PIN first before showing transaction options
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
                        showTransferPanel();
                        break;
                    case "Pay Bills":
                        showPayBillsPanel();
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
                    recordTransaction("Withdraw: PHP" + amount);
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
                recordTransaction("Deposit: PHP" + amount);
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

    // Show Bank Transfer Panel
    private void showTransferPanel() {
        frame.getContentPane().remove(mainPanel);
        frame.getContentPane().add(transferPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Process bank transfer
    private void processTransfer(String bank) {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to transfer to " + bank + ":");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                } else {
                    balance -= amount;
                    recordTransaction("Transfer to " + bank + ": PHP" + amount);
                    JOptionPane.showMessageDialog(frame, "Transfer successful to " + bank + "!");
                    checkBalance();
                    showMainPanel();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
            }
        }
    }

    // Show Pay Bills Panel
    private void showPayBillsPanel() {
        frame.getContentPane().remove(mainPanel);
        frame.getContentPane().add(payBillsPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Process bill payment
    private void processBillPayment(String biller) {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to pay for " + biller + ":");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                } else {
                    balance -= amount;
                    recordTransaction("Bill payment to " + biller + ": PHP" + amount);
                    JOptionPane.showMessageDialog(frame, "Payment successful to " + biller + "!");
                    checkBalance();
                    showMainPanel();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
            }
        }
    }

    // Show Main Panel again after transaction
    private void showMainPanel() {
        frame.getContentPane().remove(transferPanel);
        frame.getContentPane().remove(payBillsPanel);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
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
        frame.getContentPane().remove(mainPanel);
        frame.getContentPane().add(historyPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}