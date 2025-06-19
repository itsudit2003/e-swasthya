package com.raven.form;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import com.raven.model.ModelMessage;
import com.raven.model.ModelUser;
import com.raven.connection.DatabaseConnection;

public class Form_4 extends javax.swing.JPanel {
    private final ModelUser user;
    private final DefaultTableModel tableModel;
    private final JTable table;

    /**
     * Creates new form Form_4
     */
    public Form_4(ModelUser user) {
        initComponents(); // Initialize components including jComboBox1
        customizeComboBox(); // Apply custom UI for jComboBox1
        addButtons(); // Add buttons
        this.user = user;
        setDurationHint(); // Set hint for duration text field
        // Initialize table model
        tableModel = new DefaultTableModel(new Object[] { "Date", "Exercise Name", "Calories Burned" }, 0);
        // Create table and add it to scroll pane
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.BOLD, 14));
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setDefaultRenderer(Object.class, cellRenderer);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 330, 900, 360);
        scrollPane.setBackground(new Color(230, 245, 241));
        scrollPane.setForeground(new Color(7, 164, 121));
        scrollPane.getViewport().setBackground(new Color(230, 245, 241));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        // Add action listener to delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(7, 164, 121));
        deleteButton.setForeground(new Color(250, 250, 250));
        deleteButton.setFont(new Font("sansserif", Font.BOLD, 16));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });
        deleteButton.setBounds(620, 220, 100, 30);
        jPanel1.add(deleteButton);
    }

    // Method to customize JComboBox
    private void customizeComboBox() {
        // Apply custom UI for JComboBox
        jComboBox1.setUI(new ModernComboBoxUI());
        jComboBox1.setBackground(Color.WHITE); // Set background to white
    }

    // Custom ComboBoxUI class
    private class ModernComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            // Return a JButton with an arrow icon
            JButton arrowButton = new JButton();
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
            arrowButton.setContentAreaFilled(false);
            arrowButton.setBorder(BorderFactory.createEmptyBorder());
            return arrowButton;
        }

        @Override
        protected ComboPopup createPopup() {
            return new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scroller.setBorder(null);
                    scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = new Color(75, 175, 152); // Green thumb color
                        }
                    });
                    scroller.getVerticalScrollBar().setOpaque(false);
                    scroller.getViewport().setOpaque(false);
                    scroller.setOpaque(false);
                    return scroller;
                }
            };
        }

        @Override
        protected ListCellRenderer<Object> createRenderer() {
            return new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBackground(new Color(230, 245, 241)); // Light green background
                    setForeground(new Color(75, 175, 152)); // Green text color
                    setBorder(null);
                    if (isSelected) {
                        setBackground(new Color(75, 175, 152)); // Green background for selected item
                        setForeground(Color.WHITE); // White text color for selected item
                    }
                    return this;
                }
            };
        }
    }

    // Add buttons
    private void addButtons() {
        // Create and customize buttons
        JButton updateButton = new JButton("Update");
        JButton checkHistoryButton = new JButton("Check History");

        updateButton.setBackground(new Color(7, 164, 121));
        updateButton.setForeground(new Color(250, 250, 250));
        updateButton.setFont(new Font("sansserif", Font.BOLD, 16));

        checkHistoryButton.setBackground(new Color(7, 164, 121));
        checkHistoryButton.setForeground(new Color(250, 250, 250));
        checkHistoryButton.setFont(new Font("sansserif", Font.BOLD, 16));

        // Add action listeners
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelMessage message = updateHistory();
                if (message.isSuccess()) {
                    // Update was successful
                    // Handle success (if needed)
                    System.out.println(message.getMessage());
                } else {
                    // Update failed
                    // Handle failure (if needed)
                    System.err.println(message.getMessage());
                }
            }
        });

        checkHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userID = getCurrentUserid();
                if (userID != -1) {
                    ArrayList<String[]> exerciseHistory = getExerciseHistory(userID);
                    if (!exerciseHistory.isEmpty()) {
                        tableModel.setRowCount(0);
                        for (String[] entry : exerciseHistory) {
                            tableModel.addRow(entry);
                        }
                    } else {
                        System.out.println("No exercise history found for the user.");
                        JOptionPane.showMessageDialog(null, "No exercise history found for the user.", "Exercise History", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                } else {
                    System.out.println("Failed to retrieve current user's ID.");
                }
            }
        });

        // Set layout to jPanel1 as null for absolute positioning
        jPanel1.setLayout(null);

        // Set bounds for each button
        updateButton.setBounds(330, 220, 100, 30);
        checkHistoryButton.setBounds(450, 220, 150, 30);

        // Add buttons to jPanel1
        jPanel1.add(updateButton);
        jPanel1.add(checkHistoryButton);
    }

    // Set hint for duration text field
    private void setDurationHint() {
        jTextField1.setForeground(Color.black);
        jTextField1.setText("Enter Duration (in min)");
        jTextField1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jTextField1.getText().equals("Enter Duration (in min)")) {
                    jTextField1.setText("");
                    jTextField1.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jTextField1.getText().isEmpty()) {
                    jTextField1.setForeground(Color.black);
                    jTextField1.setText("Enter Duration (in min)");
                }
            }
        });
    }

    private ArrayList<String[]> getExerciseHistory(int userID) {
        ArrayList<String[]> exerciseHistory = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT h.date, e.exerciseName, e.caloriesPerMin, h.duration " +
               "FROM histories h " +
               "INNER JOIN exercises e ON h.exerciseID = e.exerciseID " +
               "WHERE h.userID = ? " +
               "ORDER BY h.date DESC";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                String exerciseName = rs.getString("exerciseName");
                int caloriesPerMin = rs.getInt("caloriesPerMin");
                int duration = rs.getInt("duration");
                int caloriesBurned = caloriesPerMin * duration;
                String[] entry = { date, exerciseName, String.valueOf(caloriesBurned) };
                exerciseHistory.add(entry);
            }
            // rs.close();
            // statement.close();
            // conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseHistory;
    }

    // Method to update history
    private ModelMessage updateHistory() {
        // Get the selected exercise from the combo box
        String exercise = (String) jComboBox1.getSelectedItem();

        // Check if the selected exercise is not null
        if (exercise == null || exercise.isEmpty() || exercise.equals("Select Workout!")) {
            JOptionPane.showMessageDialog(null, "No workout selected!", "Exercise History", JOptionPane.INFORMATION_MESSAGE);

        }

        // Initialize exerciseID
        int exerciseID = 0;

        try {
            // Query the exercises table to get the exerciseID corresponding to the selected
            // exercise name
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT exerciseID FROM exercises WHERE exerciseName = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, exercise);
            ResultSet rs = statement.executeQuery();

            // If the exerciseID is found, assign it to exerciseID variable
            if (rs.next()) {
                exerciseID = rs.getInt("exerciseID");
            }

            // Close resources
            // rs.close();
            // statement.close();
            // conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ModelMessage(false, "Error querying database.");
        }

        // If exerciseID is still 0, it means the exercise does not exist in the
        // exercises table
        if (exerciseID == 0) {
            return new ModelMessage(false, "Exercise does not exist.");
        }

        // Get duration from text field
        int duration = 0;
        try {
            duration = Integer.parseInt(jTextField1.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid duration!", "Exercise History", JOptionPane.INFORMATION_MESSAGE);

        }

        // Perform database operations using the obtained exerciseID and duration
        try {
            // Insert into histories table
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO histories (userID, exerciseID, duration, date) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, getCurrentUserid());
            insertStatement.setInt(2, exerciseID);
            insertStatement.setInt(3, duration);
            insertStatement.setDate(4, new java.sql.Date(new Date().getTime())); // Current date
            insertStatement.executeUpdate();

            // // Close resources
            // insertStatement.close();
            // conn.close();

            return new ModelMessage(true, "History updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ModelMessage(false, "Error updating history.");
        }
    }

    // Method to get the current email of the logged-in user
    private int getCurrentUserid() {
        int email = user.getUserID();
        return email; // Placeholder value
    }

    // Method to get exerciseID from exercise name
    private int getExerciseID(String exerciseName) {
        // You may implement logic to retrieve exerciseID based on exerciseName from
        // your database
        int exerciseID = 0; // Placeholder value
        // Implement your logic here to fetch exerciseID
        return exerciseID;
    }

    // Method to delete selected row
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String date = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                Connection conn = DatabaseConnection.getInstance().getConnection();
                String query = "DELETE FROM histories WHERE userID = ? AND date = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, getCurrentUserid());
                statement.setString(2, date);
                statement.executeUpdate();
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    // This method is auto-generated by NetBeans GUI Builder
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("SansSerif", 1, 28)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Fitness Centre");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField2);
        jTextField2.setBounds(300, -10, 426, 75);

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.setFont(new java.awt.Font("SansSerif", 2, 18)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setText("Did some workout? Tell us about it! ");
        jTextField7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField7);
        jTextField7.setBounds(100, 80, 870, 44);

        jComboBox1.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Workout!", "Running",
                "Walking", "Stair climbing", "Cycling", "Swimming", "Weight training", "Aerobics", "Gymnastics",
                "Jump rope", "Badminton", "Basketball", "Football", "Tennis", "Volleyball", " " }));
        jComboBox1.setBorder(null);
        jComboBox1.setOpaque(false);
        jComboBox1.setBounds(290, 150, 200, 50);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1);

        jTextField1.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jTextField1.setPreferredSize(new java.awt.Dimension(78, 26));
        jPanel1.add(jTextField1);
        jTextField1.setBounds(540, 150, 190, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 269,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(367, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}


// edit pending option pane for other errors lmao
// pending form 1