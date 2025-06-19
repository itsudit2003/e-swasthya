package com.raven.form;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.raven.model.ModelUser;
import com.raven.swing.Button;
import com.raven.swing.MyPasswordField;
import com.raven.swing.MyTextField;
import com.raven.component.Message;


import net.miginfocom.swing.MigLayout;

 

public class Form_5 extends javax.swing.JPanel {
    private ModelUser user;
    private Message messagePanel;
    public Form_5(ModelUser user) {
        initComponents();
        this.user = user;
        addRegistrationPanel();
        messagePanel = new Message();
        add(messagePanel, "dock south");
        // String email = user.getEmail();
    }

    private void initComponents() {
        setBackground(Color.WHITE); // Set the background color of the panel to white
        setPreferredSize(new java.awt.Dimension(1080, 760));
        ModelUser user = new ModelUser(); // Create a ModelUser object
        // Set properties of user if needed
        


        jTextField1 = new javax.swing.JTextField();
        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("SansSerif", 1, 33));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Update Your Information:");
        jTextField1.setBorder(null);

        jTextField2 = new javax.swing.JTextField();
        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("SansSerif", 2, 18));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Use the form below to update your information (Note: Changes to your feed and update may need a restart)");
        jTextField2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(558, Short.MAX_VALUE))
        );

        jTextField2.getAccessibleContext().setAccessibleName("");
    }

    private void addRegistrationPanel() {
        PanelLoginAndRegister registrationPanel = new PanelLoginAndRegister(user, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform any action if needed
            }
        }, null); // Pass null for eventLogin as it's not used here

        JPanel registration = registrationPanel.createRegistrationPanel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // This action will be performed when the "Update" button is clicked
                // No specific action is needed here as the registration panel already handles the update
            }
        });

        // Add the registration panel to the form layout
        javax.swing.GroupLayout layout = (javax.swing.GroupLayout) getLayout();
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(registration, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }

    // Variables declaration - do not modify
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    
    // End of variables declaration

    // Inner class for PanelLoginAndRegister
    public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
        private ModelUser user;

        public PanelLoginAndRegister(ModelUser user, ActionListener eventRegister, ActionListener eventLogin) {
            this.user=user;
            initComponents();
        }

        public JPanel createRegistrationPanel(ActionListener eventRegister) {
            JPanel registrationPanel = new JPanel(new MigLayout("wrap", "push[center]push", "push[]15[]15[]15[]15[]15[]push"));

            registrationPanel.setBackground(Color.WHITE); // Set the background color of the panel to white


            // Add registration fields
            MyTextField txtUser = new MyTextField();
            txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/user.png")));
            txtUser.setFont(new Font("sansserif", Font.BOLD, 16));
            txtUser.setHint("Name");
            registrationPanel.add(txtUser, "w 60%, center,gaptop 1, wrap");


            MyPasswordField txtPass = new MyPasswordField();
            txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
            txtPass.setFont(new Font("sansserif", Font.BOLD, 16));
            txtPass.setHint("Password");
            registrationPanel.add(txtPass, "w 60%, center, gaptop 1,wrap");

            JTextField txtAge = new JTextField(); // Use a regular JTextField for age input
            txtAge.setFont(new Font("sansserif", Font.BOLD, 16));
            txtAge.setOpaque(false);
            txtAge.setBorder(new MyTextField().getBorder());
            txtAge.setForeground(new Color(75,175,152));
            txtAge.setBackground(new Color(230, 245, 241));
            MyTextFieldHint.installHint(txtAge, "Age (in years)", new Color(75,175,152)); // Set the hint for age field
            registrationPanel.add(txtAge, "w 60%, center, gaptop 1,wrap");

            JTextField txtWeight = new JTextField(); // Use a regular JTextField for weight input
            txtWeight.setFont(new Font("sansserif", Font.BOLD, 16));
            txtWeight.setOpaque(false);
            txtWeight.setBorder(new MyTextField().getBorder());
            txtWeight.setForeground(new Color(75,175,152));
            txtWeight.setBackground(new Color(230, 245, 241));
            MyTextFieldHint.installHint(txtWeight, "Weight (in kgs)", new Color(75,175,152)); // Set the hint for weight field
            registrationPanel.add(txtWeight, "w 60%, center, gaptop 1, wrap");

            String[] genders = { "Gender", "Male", "Female", "Other" };
            JComboBox<String> cmbGender = new JComboBox<>(genders);
            cmbGender.setForeground(new Color(75, 175, 152)); // Green text color
            cmbGender.setFont(new Font("SansSerif", Font.BOLD, 16));
            cmbGender.setBackground(new Color(230, 245, 241)); // Light green background // Corrected color code
            registrationPanel.add(cmbGender, "w 60%, center, gaptop 1, wrap");

            Button cmd = new Button();
            cmd.setBackground(new Color(7, 164, 121));
            cmd.setForeground(new Color(250, 250, 250));
            cmd.addActionListener(eventRegister);
            cmd.setFont(new Font("sansserif", Font.BOLD, 16));
            cmd.setText("Update");
            registrationPanel.add(cmd, "w 40%, h 40, span, center, wrap");

            cmd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    // Retrieve user information from fields
                    String userName = txtUser.getText().trim();
                    String email = user.getEmail().trim();
                    String password = String.valueOf(txtPass.getPassword());
                    Integer age = !txtAge.getText().equals("Age (in years)") ? Integer.parseInt(txtAge.getText()) : null;
                    Integer weight = !txtWeight.getText().equals("Weight (in kgs)") ? Integer.parseInt(txtWeight.getText()) : null;
                    String selectedGender = (String) cmbGender.getSelectedItem();

                    // Update the user information in the database
                    updateUserInDatabase(userName, email, password, age, weight, selectedGender);
                }
            });

            return registrationPanel;
        }

        private void updateUserInDatabase(String userName, String email, String password, Integer age, Integer weight,
        String gender) {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://104.215.159.178:3306/users?zeroDateTimeBehavior=convertToNull", "eswasthya",
                "Udit@0502#");
        StringBuilder queryBuilder = new StringBuilder("UPDATE user SET ");

        // Append fields to be updated if they are not empty
        if (!userName.isEmpty()) {
            queryBuilder.append("username=?, ");
        }
        if (!password.isEmpty()) {
            queryBuilder.append("password=?, ");
        }
        if (age != null) {
            queryBuilder.append("age=?, ");
        }
        if (weight != null) {
            queryBuilder.append("weight=?, ");
        }
        if (!"Gender".equals(gender)) { // Check if gender is not "Gender"
            queryBuilder.append("gender=?, ");
        }
        // Remove the trailing comma and space
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(" WHERE email=?");

        PreparedStatement preparedStatement = conn.prepareStatement(queryBuilder.toString());

        int parameterIndex = 1;
        if (!userName.isEmpty()) {
            preparedStatement.setString(parameterIndex++, userName);
        }
        if (!password.isEmpty()) {
            preparedStatement.setString(parameterIndex++, password);
        }
        if (age != null) {
            preparedStatement.setInt(parameterIndex++, age);
        }
        if (weight != null) {
            preparedStatement.setInt(parameterIndex++, weight);
        }
        if (!"Gender".equals(gender)) {
            preparedStatement.setString(parameterIndex++, gender);
        }
        preparedStatement.setString(parameterIndex, email);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("User information updated successfully.");
            messagePanel.showMessage(Message.MessageType.SUCCESS, "User information updated successfully.");
            messagePanel.setShow(true);
        } else {
            System.out.println("No user found with the provided email.");
            messagePanel.showMessage(Message.MessageType.ERROR, "Error updating user information.");
            messagePanel.setShow(true);
        }

        preparedStatement.close();
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    }
}
