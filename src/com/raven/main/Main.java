//here we import the main package of the code system set
package com.raven.main;

//importing all the required packages required to run and execute jframe form and rendering of back-end stuffs
import com.raven.component.Message;
import com.raven.component.PanelCover;
import com.raven.component.PanelLoading;
import com.raven.component.PanelLoginAndRegister;
import com.raven.component.PanelVerifyCode;
import com.raven.connection.DatabaseConnection;
import com.raven.model.ModelLogin;
import com.raven.model.ModelMessage;
import com.raven.model.ModelUser;
import com.raven.service.ServiceMail;
import com.raven.service.ServiceUser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JLayeredPane;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
 
//declaration and definition of main class extended (inherited) from swing ui framework (inbuilt). Use of instance variable  

public class Main extends javax.swing.JFrame {
private final DecimalFormat df = new DecimalFormat("##0.###", DecimalFormatSymbols.getInstance(Locale.US));
private MigLayout layout;
private PanelCover cover;
private PanelLoading loading;
private PanelVerifyCode verifyCode;
private PanelLoginAndRegister loginAndRegister;
private boolean isLogin;
private final double addSize = 30;
private final double coverSize = 40;
private final double loginSize = 60;
private ServiceUser service;

    public Main() {
        initComponents();
        init();
    }

    private void init() {
        service = new ServiceUser();
        layout = new MigLayout("fill, insets 0");
        cover = new PanelCover();
        loading = new PanelLoading();
        verifyCode = new PanelVerifyCode();
        ActionListener eventRegister = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                register();
            }
        };
        ActionListener eventLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                login();
            }
        };
        loginAndRegister = new PanelLoginAndRegister(eventRegister, eventLogin);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double fractionCover;
                double fractionLogin;
                double size = coverSize;
                if (fraction <= 0.5f) {
                    size += fraction * addSize;
                } else {
                    size += addSize - fraction * addSize;
                }
                if (isLogin) {
                    fractionCover = 1f - fraction;
                    fractionLogin = fraction;
                    if (fraction >= 0.5f) {
                        cover.registerRight(fractionCover * 100);
                    } else {
                        cover.loginRight(fractionLogin * 100);
                    }
                } else {
                    fractionCover = fraction;
                    fractionLogin = 1f - fraction;
                    if (fraction <= 0.5f) {
                        cover.registerLeft(fraction * 100);
                    } else {
                        cover.loginLeft((1f - fraction) * 100);
                    }
                }
                if (fraction >= 0.5f) {
                    loginAndRegister.showRegister(isLogin);
                }
                fractionCover = Double.valueOf(df.format(fractionCover));
                fractionLogin = Double.valueOf(df.format(fractionLogin));
                layout.setComponentConstraints(cover, "width " + size + "%, pos " + fractionCover + "al 0 n 100%");
                layout.setComponentConstraints(loginAndRegister, "width " + loginSize + "%, pos " + fractionLogin + "al 0 n 100%");
                bg.revalidate();
            }

            @Override
            public void end() {
                isLogin = !isLogin;
            }
        };

    //this part of code helps in transition
        Animator animator = new Animator(800, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);  //  for smooth animation
        bg.setLayout(layout);
        bg.setLayer(loading, JLayeredPane.POPUP_LAYER);
        bg.setLayer(verifyCode, JLayeredPane.POPUP_LAYER);
        bg.add(loading, "pos 0 0 100% 100%");
        bg.add(verifyCode, "pos 0 0 100% 100%");
        bg.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        bg.add(loginAndRegister, "width " + loginSize + "%, pos 1al 0 n 100%"); //  1al as 100%
        cover.addEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }
            }
        });
        verifyCode.addEventButtonOK(new ActionListener() {
            @Override
      public void actionPerformed(ActionEvent ae) {
    try {
        ModelUser user = loginAndRegister.getUser();

        // Debug statement to check the state of the user object before verification
        System.out.println("User ID before verification: " + user.getUserID());

        if (user.getUserID() != 0) {
            // If the user ID is not 0, proceed with verification
            if (service.verifyCodeWithUser(user.getUserID(), verifyCode.getInputCode())) {
                // Debug statement for successful verification
                System.out.println("Verification successful for user ID: " + user.getUserID());

                service.doneVerify(user.getUserID());
                showMessage(Message.MessageType.SUCCESS, "Register success");
                System.out.println("User ID after registration: " + user.getUserID());
                verifyCode.setVisible(false);
            } else {
                // Debug statement for unsuccessful verification
                System.out.println("Verification failed for user ID: " + user.getUserID());

                showMessage(Message.MessageType.ERROR, "Verify code incorrect");
            }
        } else {
            // Handle the case where the user ID is 0
            showMessage(Message.MessageType.ERROR, "Invalid user ID for verification");
        }
    } catch (SQLException e) {
        showMessage(Message.MessageType.ERROR, "Error. Please Retry");
        e.printStackTrace();
    }
}


        });
    }

    private void register() {
        ModelUser user = loginAndRegister.getUser();
//        ModelUser user = loginAndRegister.getUser();
System.out.println("User ID before verification: " + user.getUserID());
        try {
            if (service.checkDuplicateUser(user.getUserName())) {
                showMessage(Message.MessageType.ERROR, "User name already exists");
            } else if (service.checkDuplicateEmail(user.getEmail())) {
                showMessage(Message.MessageType.ERROR, "Email already exists");
            } else {
                service.insertUser(user);
                sendMain(user);
            }
        } catch (SQLException e) {
            showMessage(Message.MessageType.ERROR, "Error Register");
            e.printStackTrace();
        }
    }

private void login() {
    ModelLogin data = loginAndRegister.getDataLogin();
    try {
        ModelUser user = service.login(data);
        if (user != null) {
            this.dispose();
            MainSystem.main(user);
        } else {
            showMessage(Message.MessageType.ERROR, "Email and Password incorrect");
        }
    } catch (Exception e) {
        showMessage(Message.MessageType.ERROR, "Error Login");
    }
}   

    private void sendMain(ModelUser user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loading.setVisible(true);
                ModelMessage ms = new ServiceMail().sendMain(user.getEmail(), user.getVerifyCode());
                if (ms.isSuccess()) {
                    loading.setVisible(false);
                    verifyCode.setVisible(true);
                } else {
                    loading.setVisible(false);
                    showMessage(Message.MessageType.ERROR, ms.getMessage());
                }
            }
        }).start();
    }

    private void showMessage(Message.MessageType messageType, String message) {
        Message ms = new Message();
        ms.showMessage(messageType, message);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                if (!ms.isShow()) {
                    bg.add(ms, "pos 0.5al -30", 0); //  Insert to bg fist index 0
                    ms.setVisible(true);
                    bg.repaint();
                }
            }

            @Override
            public void timingEvent(float fraction) {
                float f;
                if (ms.isShow()) {
                    f = 40 * (1f - fraction);
                } else {
                    f = 40 * fraction;
                }
                layout.setComponentConstraints(ms, "pos 0.5al " + (int) (f - 30));
                bg.repaint();
                bg.revalidate();
            }

            @Override
            public void end() {
                if (ms.isShow()) {
                    bg.remove(ms);
                    bg.repaint();
                    bg.revalidate();
                } else {
                    ms.setShow(true);
                }
            }
        };
        Animator animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    animator.start();
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        }).start();
    }
    public void displayTray() throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("Eswasthya", "Time to take a water and screen break!", MessageType.INFO);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 933, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) throws AWTException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    // notification td = new notification();
                    try {
                        new Main().displayTray();
                    } catch (AWTException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("System tray not supported!");
                }
            }
          };
        
          timer.schedule(task, 10 * 1000, 1800 * 1000);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            DatabaseConnection.getInstance().connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    
    private javax.swing.JLayeredPane bg;
   
}
