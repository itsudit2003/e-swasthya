package com.raven.main;

import com.raven.component.menu;
import com.raven.event.EventMenuSelected;
import com.raven.form.Form_1;
import com.raven.form.Form_2;
import com.raven.form.Form_3;
import com.raven.form.Form_4;
import com.raven.form.Form_5;
import com.raven.form.Form_Home;
import com.raven.model.ModelUser;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

public class MainSystem extends javax.swing.JFrame {

     private final ModelUser user;
    private Form_2 form2; // Declare Form_2 instance


    public MainSystem(ModelUser user) {
        this.user = user;
        initComponents();
        getContentPane().setBackground(new Color(0, 0, 0));
        // Initialize Form_2 instance
        form2 = new Form_2();
        menu2.initMoving(MainSystem.this);
        setForm(new Form_Home(user));
        menu2.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                if (index == 1) {
                    System.out.println("page 1");
                    setForm(new Form_1());
                } else if (index == 2) {
                    // Use Form_2 instance
                    System.out.println("page 2");
                    setForm(form2);
                } else if (index == 3) {
                    System.out.println("page 3");
                    setForm(new Form_3(new ModelUser(), new ModelUser()));
                } else if (index == 4) {
                    System.out.println("page 4");
                    setForm(new Form_4(user));
                } else if (index == 7) {
                    System.out.println("page 5");
                    setForm(new Form_5(user));
                    
                }
            }
        });
    
//        setBackground(new Color(0,0,0,0));
    }
    private void setForm(JComponent com){
        mainPanel1.removeAll();
        mainPanel1.add(com);
        mainPanel1.repaint();
        mainPanel1.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        panelborder1 = new com.raven.swing.panelborder();
        menu2 = new com.raven.component.menu();
        mainPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setMinimumSize(new java.awt.Dimension(1400, 740));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        panelborder1.setBackground(new java.awt.Color(255, 255, 255));
        panelborder1.setMaximumSize(new java.awt.Dimension(1400, 740));
        panelborder1.setOpaque(true);
        panelborder1.setLayout(null);
        panelborder1.add(menu2);
        menu2.setBounds(10, 0, 330, 740);

        mainPanel1.setOpaque(false);
        panelborder1.add(mainPanel1);
        mainPanel1.setBounds(340, 0, 1060, 730);

        getContentPane().add(panelborder1);
        panelborder1.setBounds(-10, 0, 1410, 760);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(ModelUser user) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainSystem(user).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mainPanel1;
    private com.raven.component.menu menu2;
    private com.raven.swing.panelborder panelborder1;
    // End of variables declaration//GEN-END:variables
}
