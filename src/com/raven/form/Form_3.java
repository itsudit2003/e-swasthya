package com.raven.form;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.raven.model.ModelUser;

public class Form_3 extends javax.swing.JPanel {
    private EdamamRecipeFinder edamamAPI;

    /**
     * Creates new form Form_3
     */
    public Form_3(ModelUser age, ModelUser weight) {
        // Call initComponents method to initialize components
        initComponents();
    
        // Initialize the EdamamRecipeFinder
        edamamAPI = new EdamamRecipeFinder();
    
        // Display the recipes
        displayRecipes();
        // click able urls
        addMouseListeners();

    }
    

    private void displayRecipes() {
        try {
            // Retrieve recipes from the API
            List<String> recipes = edamamAPI.getRecipes();

            // Display the retrieved recipes in respective text areas
            if (recipes != null && recipes.size() >= 4) {
                jTextArea1.setText(recipes.get(0));
                jTextArea2.setText(recipes.get(1));
                jTextArea3.setText(recipes.get(2));
                jTextArea4.setText(recipes.get(3));
            } else {
                // Handle case when no recipes are retrieved or there are fewer than 4 recipes
                // You can set a default message or do something else here
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // You can also display an error message to the user if needed
            // For example:
            // JOptionPane.showMessageDialog(this, "An error occurred while fetching recipes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     private void addMouseListeners() {
        // Mouse listener for jTextArea1
        jTextArea1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRecipeUrl(jTextArea1.getText());
            }
        });

        // Mouse listener for jTextArea2
        jTextArea2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRecipeUrl(jTextArea2.getText());
            }
        });

        // Mouse listener for jTextArea3
        jTextArea3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRecipeUrl(jTextArea3.getText());
            }
        });

        // Mouse listener for jTextArea4
        jTextArea4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRecipeUrl(jTextArea4.getText());
            }
        });
    }

    private void openRecipeUrl(String recipeData) {
        // Extract URL from recipeData
        String[] lines = recipeData.split("\n");
        String recipeUrl = lines[1].substring("Recipe Url: ".length());

        // Open URL in default browser
        try {
            Desktop.getDesktop().browse(new URI(recipeUrl));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        

        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1080, 760));
        setLayout(null);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("SansSerif", 1, 33)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Recipes for You");
        jTextField1.setBorder(null);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        add(jTextField1);
        jTextField1.setBounds(15, 29, 1073, 42);

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("SansSerif", 2, 18)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Below are some recipes personalised just for you!");
        jTextField2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        add(jTextField2);
        jTextField2.setBounds(15, 89, 1073, 47);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(230, 245, 241));
        jTextArea1.setColumns(28);
        jTextArea1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(7, 164, 121));
        jTextArea1.setRows(15);
        jScrollPane1.setViewportView(jTextArea1);

        add(jScrollPane1);
        jScrollPane1.setBounds(10, 440, 530, 270);

        jTextArea2.setEditable(false);
        jTextArea2.setBackground(new java.awt.Color(230, 245, 241));
        jTextArea2.setColumns(28);
        jTextArea2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(7, 164, 121));
        jTextArea2.setRows(15);
        jScrollPane2.setViewportView(jTextArea2);

        add(jScrollPane2);
        jScrollPane2.setBounds(560, 440, 530, 270);

        jTextArea3.setEditable(false);
        jTextArea3.setBackground(new java.awt.Color(230, 245, 241));
        jTextArea3.setColumns(28);
        jTextArea3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextArea3.setForeground(new java.awt.Color(7, 164, 121));
        jTextArea3.setRows(15);
        jScrollPane3.setViewportView(jTextArea3);

        add(jScrollPane3);
        jScrollPane3.setBounds(10, 140, 530, 270);

        jTextArea4.setEditable(false);
        jTextArea4.setBackground(new java.awt.Color(230, 245, 241));
        jTextArea4.setColumns(28);
        jTextArea4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextArea4.setForeground(new java.awt.Color(7, 164, 121));
        jTextArea4.setRows(15);
        jScrollPane4.setViewportView(jTextArea4);

        add(jScrollPane4);
        jScrollPane4.setBounds(560, 140, 530, 270);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
