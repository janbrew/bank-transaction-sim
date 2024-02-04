package net.jsf.face;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import net.jsf.brain.Bank;
import net.jsf.exceptions.AccountNotFound;
import net.jsf.exceptions.DepositLimitReached;
import net.jsf.exceptions.InsufficientAccountBalance;
import net.miginfocom.swing.MigLayout;


class RoundedTextField extends JTextField {
    private int radius = 15;
    private Color backgroundColor = null;
    private Color borderColor = null;
    private Dimension corners = new Dimension(radius, radius);
    private Shape shape;

    public RoundedTextField(Color backgroundColor, Color borderColor) {
        setOpaque(false);
        setHorizontalAlignment(JTextField.CENTER);
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(this.backgroundColor);
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, corners.width, corners.height);
        super.paintComponent(graphics);
    }

    @Override
    protected void paintBorder(Graphics graphics) {
        graphics.setColor(this.borderColor);
        graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, corners.width, corners.height);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, corners.width, corners.height);
        }
        return shape.contains(x, y);
    }
}

public class Visual extends JFrame {
    private final Dimension WINDOWSIZE = new Dimension(1280, 700);
    private final String WINDOWTITLE = "Bank Transaction Simulation";
    private final Image WINDOWICON = new ImageIcon("logo.png").getImage();

    private RoundedTextField accountNumberField;
    private JPanel interactionPanel;
    private JPanel horizontalInteractionPanel;
    private JLabel loginLabel;
    private JPanel loginPanel;
    private JPanel screenPanel;
    private JPanel actionsPanel;
    private JPanel sidebarPanel;
    private JPanel outputPanel;
    private JPanel outputArea;
    private JPanel withdrawButton;
    private JPanel depositButton;
    private JPanel balanceButton;
    private JPanel powerOffButton;
    private JLabel hoverActionLabel;

    private JTextField moneyField;
    private String currentTransaction = "";

    private Box.Filler screenBottomFiller;
    private Box.Filler screenLeftFiller;
    private Box.Filler screenRightFiller;
    private Box.Filler screenTopFiller;

    private Color numPanelBackground = new Color(53, 50, 190);
    private Color screenPanelBackground = new Color(218, 218, 242);
    private Color actionButtonBackground = new Color(53, 50, 190).darker();
    private Color textForeground = new Color(230, 230, 230);
    private Color screenTextForeground = new Color(50, 60, 110);
    private Color outputPanelBackground = screenPanelBackground;

    private Bank bank = null;

    public Visual() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(WINDOWTITLE);
        setIconImage(WINDOWICON);
        setPreferredSize(WINDOWSIZE);

        initComponents();
        initInteractionPanel();
        initScreenPanel();

        pack();
        setVisible(true);
    }

    private void initInteractionPanel() {
        interactionPanel.setBackground(numPanelBackground);
        interactionPanel.setPreferredSize(new Dimension(450, 500));
        interactionPanel.setLayout(new MigLayout("wrap, align 50% 50%, fill", "[]30[]30[]", "[]30[]30[]30[]"));
        
        initNumButton("1");
        initNumButton("2");
        initNumButton("3");
        initNumButton("4");
        initNumButton("5");
        initNumButton("6");
        initNumButton("7");
        initNumButton("8");
        initNumButton("9");
        initNumButton("Clr");
        initNumButton("0");
        initNumButton("Entr");

        getContentPane().add(interactionPanel, BorderLayout.LINE_END);
        }

    private void initScreenPanel() {
        screenPanel.setBackground(screenPanelBackground);
        screenPanel.setLayout(new BorderLayout());
        screenPanel.add(screenRightFiller, BorderLayout.LINE_END);
        screenPanel.add(screenLeftFiller, BorderLayout.LINE_START);
        screenPanel.add(screenTopFiller, BorderLayout.PAGE_START);
        screenPanel.add(screenBottomFiller, BorderLayout.PAGE_END);

        loginPanel.setBackground(screenPanelBackground);
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 100));

        loginLabel.setFont(new Font("OCR A Extended", 1, 36));
        loginLabel.setText("Enter your Account Number");
        loginLabel.setForeground(new Color(63, 63, 107));
        loginPanel.add(loginLabel);

        accountNumberField = new RoundedTextField(numPanelBackground, screenPanelBackground);
        accountNumberField.setEditable(false);
        accountNumberField.setBackground(numPanelBackground);
        accountNumberField.setFont(new Font("Monospaced", 1, 36));
        accountNumberField.setForeground(textForeground);
        accountNumberField.setHorizontalAlignment(JTextField.CENTER);
        accountNumberField.setBorder(null);
        accountNumberField.setFocusable(false);
        accountNumberField.setPreferredSize(new Dimension(600, 80));
        accountNumberField.setRequestFocusEnabled(false);
        loginPanel.add(accountNumberField);

        screenPanel.add(loginPanel, BorderLayout.CENTER);

        getContentPane().add(screenPanel, BorderLayout.CENTER);
    }
    
    private void initSidebarPanel() {
        remove(interactionPanel);

        sidebarPanel = new JPanel(new MigLayout("wrap, fill", "[]", "[]10[]100[]"));
        sidebarPanel.setBackground(numPanelBackground);
        sidebarPanel.setPreferredSize(new Dimension(400, HEIGHT));
        JLabel welcomeText = new JLabel("Welcome,");
        JLabel accountNum = new JLabel(Bank.formatAccountNumber("censored") + "!");

        welcomeText.setFont(new Font("Bahnschrift", 1, 36));
        welcomeText.setForeground(new Color(230, 230, 230));

        accountNum.setFont(new Font("Bahnschrift", 1, 36));
        accountNum.setForeground(new Color(230, 230, 230));

        sidebarPanel.add(welcomeText);
        sidebarPanel.add(accountNum);

        add(sidebarPanel, BorderLayout.EAST);

        initActionsPanel();

        repaint();
        revalidate();
    }

    private void initActionsPanel() {
        withdrawButton = initActionButton("Withdraw");
        depositButton = initActionButton("Deposit");   
        balanceButton = initActionButton("Balance");   
        powerOffButton = initActionButton("Exit");
        
        withdrawButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                onMoneyActionClick(evt);
            }
        });

        depositButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                onMoneyActionClick(evt);
            }
        });

        balanceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                onBalanceClick(evt);
            }
        });

        powerOffButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                onExitClick(evt);
            }
        });

        actionsPanel = new JPanel(new MigLayout("wrap, align 50% 50%", "", "[]30[]30[]30[]10"));

        actionsPanel.setBackground(numPanelBackground);
        
        actionsPanel.add(withdrawButton);
        actionsPanel.add(depositButton);
        actionsPanel.add(balanceButton);
        actionsPanel.add(powerOffButton);

        sidebarPanel.add(actionsPanel);
    }

    private void initOutputPanel() {
        outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(outputPanelBackground);
        outputArea = new JPanel(new MigLayout("wrap, align center", "", "[][]"));
        outputArea.setBackground(outputPanelBackground);

        outputPanel.add(outputArea, BorderLayout.CENTER);

        screenPanel.remove(loginPanel);
        screenPanel.add(outputPanel, BorderLayout.CENTER);

        revalidate();
    }

    private JPanel initActionButton(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panel.setPreferredSize(new Dimension(400, 60));
        panel.setBackground(actionButtonBackground);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", 1, 24));
        label.setForeground(textForeground);
        panel.add(label);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                onActionButtonEnter(evt);
            }
            public void mouseExited(MouseEvent evt) {
                onActionButtonExit(evt);
            }
            public void mouseClicked(MouseEvent evt) {
                onActionButtonClick(evt);
            }
        });
        return panel;
    }

    private void initNumButton(String text) {
        JPanel panel = new JPanel(new MigLayout("fill, wrap"));
        JLabel label = new JLabel();
        panel.setBackground(numPanelBackground);

        panel.setPreferredSize(new Dimension(150, 150));
        label.setFont(new Font("OCR A Extended", 1, 36));
        label.setForeground(new Color(230, 230, 230));

        label.setText(text);
        panel.add(label, "align 50% 50%");
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                onNumButtonEnter(evt);
            }
            public void mouseExited(MouseEvent evt) {
                onNumButtonExit(evt);
            }
            public void mouseClicked(MouseEvent evt) {
                onNumButtonClick(evt);
            }
        });
        interactionPanel.add(panel);
    }

    private void initMoneyTransactionField(String label) {
        JLabel moneyLabel = new JLabel("Enter your " +  label + " Amount");
        moneyLabel.setFont(new Font("Monospaced", 1, 26));
        moneyLabel.setForeground(screenTextForeground);

        moneyField = new RoundedTextField(numPanelBackground, screenPanelBackground);
        moneyField.setEditable(false);
        moneyField.setHorizontalAlignment(JTextField.CENTER);
        moneyField.setBackground(numPanelBackground);
        moneyField.setFont(new Font("Monospaced", 1, 18));
        moneyField.setForeground(textForeground);
        moneyField.setHorizontalAlignment(JTextField.CENTER);
        moneyField.setBorder(null);
        moneyField.setFocusable(false);
        moneyField.setPreferredSize(new Dimension(600, 40));
        moneyField.setRequestFocusEnabled(false);

        outputArea.add(moneyLabel, "align center");
        outputArea.add(moneyField, "align center");
        
        revalidate();
    }

    private void initComponents() {
        interactionPanel = new JPanel();
        screenPanel = new JPanel();
        screenRightFiller = new Box.Filler(new Dimension(0, 0), new Dimension(100, 0), new Dimension(32767, 0));
        screenLeftFiller = new Box.Filler(new Dimension(0, 0), new Dimension(100, 0), new Dimension(32767, 0));
        screenTopFiller = new Box.Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 32767));
        screenBottomFiller = new Box.Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 32767));
        loginPanel = new JPanel();
        loginLabel = new JLabel();
    }

    private void onNumButtonEnter(MouseEvent evt) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void onNumButtonExit(MouseEvent evt) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        JPanel panel = (JPanel) evt.getSource();

        panel.setBackground(numPanelBackground);
    }

    private void onNumButtonClick(MouseEvent evt) {
        String fieldText = accountNumberField.getText().replaceAll(" ", "");
        
        if (fieldText.length() > 9) {
            new Thread(() -> {
                accountNumberField.setEditable(true);
                accountNumberField.setFont(new Font("Monospaced", 1, 20));
                accountNumberField.setText("Account Number must not be greater than 9 digits");

                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } 

                accountNumberField.setFont(new Font("Monospaced", 1, 36));
                accountNumberField.setText("");
                
            }).start();

            return;
        }   

        JPanel panel = (JPanel) evt.getSource();

        panel.setBackground(numPanelBackground.darker());
    
        JLabel label = (JLabel) panel.getComponent(0);
    
        accountNumberField.setEditable(true);
    
        if (label.getText() != "Clr" && label.getText() != "Entr") {
                
            if (fieldText.length() == 3 || fieldText.length() == 6) {
                accountNumberField.setText(accountNumberField.getText() + " ");
            }
            accountNumberField.setText(accountNumberField.getText() + label.getText());
        }
        else if (label.getText() == "Clr") {
            accountNumberField.setText("");
        }
        else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            try {
                bank = new Bank(Integer.parseInt(accountNumberField.getText().replaceAll(" ", "")));

                new Thread(() -> {
                    initOutputPanel();
                    initSidebarPanel();
                }).start();
            }

            catch (AccountNotFound error) {
                new Thread(() -> {
                    accountNumberField.setEditable(true);
                    accountNumberField.setText(error.getMessage());
                    accountNumberField.setFont(new Font("Monospaced", 1, 22));
    
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
    
                    accountNumberField.setText("");
                    accountNumberField.setFont(new Font("Monospaced", 1, 36));
                    accountNumberField.setForeground(textForeground);
                    accountNumberField.setEditable(false);
                }).start();
            }
        }
        accountNumberField.setEditable(false); 
    }

    private void onActionButtonEnter(MouseEvent evt) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panel = (JPanel) evt.getSource();
        panel.setBackground(actionButtonBackground.darker());

    }

    private void onActionButtonExit(MouseEvent evt) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        JPanel panel = (JPanel) evt.getSource();

        panel.setBackground(actionButtonBackground);
    }

    private void onActionButtonClick(MouseEvent evt) {
        try {
            outputPanel.remove(horizontalInteractionPanel);
        }
        catch (Exception e) {
            
        }

        outputArea.removeAll();

        JPanel panel = (JPanel) evt.getSource();

        hoverActionLabel = new JLabel(((JLabel) panel.getComponent(0)).getText());
        hoverActionLabel.setForeground(screenTextForeground);
        hoverActionLabel.setFont(new Font("Monospaced", 1, 48));

        outputArea.add(hoverActionLabel, "align center");
    }

    private void onExitClick(MouseEvent evt) {
        dispose();
        System.exit(0);
    }

    private void onBalanceClick(MouseEvent evt) {
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Monospaced", 0, 22));
        resultArea.setEnabled(true);
        resultArea.setBackground(outputPanelBackground);

        long balance = bank.seeBalance();
        int interest = bank.calculateInterest();
        
        resultArea.setText("Account Balance: " + Bank.formatAccountBalance(balance) + "\n" +
                            "Interest Rate: " + Bank.formatAccountBalance(interest) + "\n\n" +
                            "Total Balance: " +Bank.formatAccountBalance(balance + interest));

        resultArea.setForeground(screenTextForeground);
        resultArea.setEditable(false);
        outputArea.add(resultArea, "align center");

        repaint();
        revalidate();
    }

    private void onMoneyActionClick(MouseEvent evt) {
        try {
            outputPanel.remove(horizontalInteractionPanel);
        }
        catch (Exception e) {

        }
        initHorizontalInteractionPanel();

        JLabel label = ((JLabel) ((JPanel) evt.getSource()).getComponent(0));

        if (label.getText().equalsIgnoreCase("Deposit")) {
            initMoneyTransactionField("Deposit");
            currentTransaction = "D";
        }
        else {
            initMoneyTransactionField("Withdraw");
            currentTransaction = "W";
        }
    }

    private void initHorizontalInteractionPanel() {
        horizontalInteractionPanel = new JPanel(new MigLayout("wrap, align 50% 50%, fill", "[]10[]10[]10[]10[]10[]", "[]10[]"));
        horizontalInteractionPanel.setBackground(numPanelBackground);
        horizontalInteractionPanel.setPreferredSize(new Dimension(300, 200));
        
        initHorizontalNumButton("1");
        initHorizontalNumButton("2");
        initHorizontalNumButton("3");
        initHorizontalNumButton("4");
        initHorizontalNumButton("5");
        initHorizontalNumButton("6");
        initHorizontalNumButton("7");
        initHorizontalNumButton("8");
        initHorizontalNumButton("9");
        initHorizontalNumButton("Clr");
        initHorizontalNumButton("0");
        initHorizontalNumButton("Entr");

        outputPanel.add(horizontalInteractionPanel, BorderLayout.SOUTH);
    }

    private void initHorizontalNumButton(String text) {
        JPanel panel = new JPanel(new MigLayout("fill, wrap"));
        JLabel label = new JLabel();
        panel.setBackground(numPanelBackground);

        panel.setPreferredSize(new Dimension(50, 50));
        label.setFont(new Font("OCR A Extended", 1, 24));
        label.setForeground(new Color(230, 230, 230));

        label.setText(text);
        panel.add(label, "align 50% 50%");
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                onNumButtonEnter(evt);
            }
            public void mouseExited(MouseEvent evt) {
                onNumButtonExit(evt);
            }
            public void mouseClicked(MouseEvent evt) {
                onHorizontalNumButtonClick(evt);
            }
        });
        horizontalInteractionPanel.add(panel);
    }

    private void onHorizontalNumButtonClick(MouseEvent evt) {
        JPanel panel = (JPanel) evt.getSource();

        panel.setBackground(numPanelBackground.darker());

        JLabel label = (JLabel) panel.getComponent(0);

        if (label.getText().equalsIgnoreCase("Entr")) {
            if (!moneyField.getText().isEmpty()) {
                if (moneyField.getText().startsWith("0")) {
                    new Thread(() -> {
                        moneyField.setText("Enter an amount starting from $ 1");
                        
                        try {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                        moneyField.setText("");
                    }).start();

                    return;
                }

            
                long amount = 0;

                outputPanel.remove(horizontalInteractionPanel);
    
                if (currentTransaction.equals("D")) {
                    amount = Long.parseLong(moneyField.getText());
                    
                    try {
                        bank.depositCash(amount);
    
                        JLabel balanceLabel = new JLabel("Updated Balance: " + Bank.formatAccountBalance(bank.seeBalance()));
                        balanceLabel.setFont(new Font("Monospaced", 0, 22));
                        balanceLabel.setForeground(screenTextForeground);
            
                        outputArea.add(balanceLabel, "align center, gaptop 50");
                    }
    
                    catch (DepositLimitReached err) {
                        new Thread(() -> {
                            moneyField.setFont(new Font("Monospaced", 1, 14));
                            moneyField.setText(err.getMessage());
    
                            try {
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            moneyField.setText("");
    
                        }).start();
                    }
                }
                
                else if (currentTransaction.equals("W")) {
                     amount = Long.parseLong(moneyField.getText());
    
                    try {
                        bank.withdrawCash(amount);
    
                        JLabel balanceLabel = new JLabel("Updated Balance: " + Bank.formatAccountBalance(bank.seeBalance()));
                        balanceLabel.setFont(new Font("Monospaced", 0, 22));
                        balanceLabel.setForeground(screenTextForeground);
            
                        outputArea.add(balanceLabel, "align center, gaptop 50");
                    }
                    
                    catch (InsufficientAccountBalance err) {
                        new Thread(() -> {
                            moneyField.setFont(new Font("Monospaced", 1, 14));
                            moneyField.setText(err.getMessage());
    
                            try {
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            moneyField.setText("");
    
                        }).start();
                    }
                }
                revalidate();
            }
        }
        
        else if (label.getText().equalsIgnoreCase("Clr")) {
            if (moneyField != null) 
                moneyField.setText("");
            else
                moneyField.setText("");
        }
        
        else {
            if (moneyField != null)
                moneyField.setText(moneyField.getText() + label.getText());
            else   
                moneyField.setText(moneyField.getText() + label.getText());
        }
    }

}
