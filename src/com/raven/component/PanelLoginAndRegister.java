package com.raven.component;

import com.raven.model.ModelLogin;
import com.raven.model.ModelUser;
import com.raven.swing.Button;
import com.raven.swing.MyPasswordField;
import com.raven.swing.MyTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    public ModelLogin getDataLogin() {
        return dataLogin;
    }

    public ModelUser getUser() {
        return user;
    }

    private ModelUser user;
    private ModelLogin dataLogin;

    public PanelLoginAndRegister(ActionListener eventRegister, ActionListener eventLogin) {
        initComponents();
        initRegister(eventRegister);
        initLogin(eventLogin);
        login.setVisible(false);
        register.setVisible(true);
    }

    class IntegerOnlyTextField extends JPanel {
        private final String hint;
        private final JTextField textField;

        public String getText() {
            return textField.getText();
        }

        public IntegerOnlyTextField(String hint) {
            this.hint = hint;

            setLayout(new BorderLayout());
            setOpaque(false); // Ensure the JPanel itself is not visible

            textField = new JTextField(hint);
            textField.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
            // textField.setBackground(new Color(0, 0, 0, 0));
            textField.setForeground(Color.decode("#4BAF98"));
            textField.setFont(new java.awt.Font("sansserif", Font.BOLD, 16));
            textField.setBackground(Color.decode("#E6F5F2"));
            // Set initial hint text
            textField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (textField.getText().equals(hint)) {
                        textField.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (textField.getText().isEmpty()) {
                        textField.setText(hint);
                    }
                }
            });

            textField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            add(textField);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (textField.getText().isEmpty() && !textField.hasFocus()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 50)); // Adjust the alpha value as needed
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        }
    }

    private void initRegister(ActionListener eventRegister) {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]15[]15[]15[]15[]15[]push"));
        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", Font.BOLD, 32));
        label.setForeground(new Color(7, 164, 121));
        register.add(label, "span, center, wrap"); // span the label across all columns and move to the next row

        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/user.png")));
        txtUser.setFont(new Font("sansserif", Font.BOLD, 16));
        txtUser.setHint("Name");
        register.add(txtUser, "w 60%, center, wrap");

        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setFont(new Font("sansserif", Font.BOLD, 16));
        txtEmail.setHint("Email");
        register.add(txtEmail, "w 60%, center, wrap");

        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setFont(new Font("sansserif", Font.BOLD, 16));
        txtPass.setHint("Password");
        register.add(txtPass, "w 60%, center, wrap");

        IntegerOnlyTextField txtAge = new IntegerOnlyTextField("Age (in years)");
        register.add(txtAge, "w 60%, center, wrap");

        IntegerOnlyTextField txtWeight = new IntegerOnlyTextField("Weight (in kgs)");
        register.add(txtWeight, "w 60%, center, gaptop 2, wrap");

        String[] genders = { "Gender", "Male", "Female", "Other" };
        JComboBox<String> cmbGender = new JComboBox<>(genders);
        cmbGender.setForeground(new Color(75, 175, 152)); // Green text color
        cmbGender.setFont(new Font("SansSerif", Font.BOLD, 16));
        cmbGender.setBackground(new Color(230, 245, 241)); // Light green background // Corrected color code
        register.add(cmbGender, "w 60%, center, gaptop 1, wrap");

        Button cmd = new Button();
        cmd.setBackground(new Color(7, 164, 121));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(eventRegister);
        cmd.setFont(new Font("sansserif", Font.BOLD, 16));
        cmd.setText("Sign Up");
        register.add(cmd, "w 40%, h 40, span, center, wrap");

        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String userName = txtUser.getText().trim();
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPass.getPassword());
                // Retrieve age and weight from IntegerOnlyTextField
                Integer age = null;
                if (!txtAge.getText().equals("Age")) {
                    age = Integer.parseInt(txtAge.getText());
                }
                Integer weight = null;
                if (!txtWeight.getText().equals("Weight")) {
                    weight = Integer.parseInt(txtWeight.getText());
                }
                // Retrieve gender from JComboBox
                String selectedGender = (String) cmbGender.getSelectedItem();

                // Create ModelUser object with all the information
                user = new ModelUser(0, userName, email, password, "", "", age, selectedGender, weight);
            }
        });
    }

    private void initLogin(ActionListener eventLogin) {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("SIGN IN");
        label.setFont(new Font("sansserif", Font.BOLD, 30));

        label.setForeground(new Color(7, 164, 121));
        login.add(label);
        MyTextField txtEmail = new MyTextField();

        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setFont(new Font("sansserif", Font.BOLD, 16));
        txtEmail.setHint("Email");
        login.add(txtEmail, "w 60%");

        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setFont(new Font("sansserif", Font.BOLD, 16));
        txtPass.setHint("Password");
        login.add(txtPass, "w 60%");

        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", Font.BOLD, 15));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdForget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the "Forgot Password" webpage in the default web browser
                openForgotPasswordWebpage();
            }
        });
        login.add(cmdForget);
        Button cmd = new Button();
        cmd.setBackground(new Color(7, 164, 121));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(eventLogin);
        cmd.setText("SIGN IN");
        cmd.setFont(new Font("sansserif", Font.BOLD, 16));
        login.add(cmd, "w 40%, h 40");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPass.getPassword());
                dataLogin = new ModelLogin(email, password);
            }
        });
    }
    private void openForgotPasswordWebpage() {
        try {
            
            String webpageURL = "http://eswasthya.hopto.org/";
            Desktop.getDesktop().browse(java.net.URI.create(webpageURL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE));
        loginLayout.setVerticalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE));
        registerLayout.setVerticalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
