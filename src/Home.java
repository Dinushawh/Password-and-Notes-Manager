
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dinus
 */
public final class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    
    Connection con = null;
    Statement stmt = null;
    
    public static String userid;
    
    public Home() {
        
        FlatLightLaf.setup();
        initComponents();
        
        PasswordList view = new PasswordList ();
        jDesktopPane1.removeAll();
        jDesktopPane1.add(view).setVisible(true);
        
        con = Database_connection_CLASS.connection();
        LoggedUser();
    }
    
    public void LoggedUser(){
    
        try{
            
            stmt = con.createStatement();
         
            String query = "SELECT * FROM users ";
            ResultSet rs = stmt.executeQuery(query);

                while(rs.next())
                {
                    if(userid.equals(rs.getString(2))){
                    
                        jLabel1.setText(rs.getString(3)+" "+rs.getString(4));
                    }
                    
                    
                }
        
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void backgroundColor(){
    
        try{
        
        }
        catch(Exception e){
        
            
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        myPasswords = new javax.swing.JButton();
        dashBoard1 = new javax.swing.JButton();
        addnew = new javax.swing.JButton();
        settings = new javax.swing.JButton();
        addnew1 = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("jLabel1");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/faltLogo-01.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/USER-01.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 657, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1500, 80));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        myPasswords.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        myPasswords.setForeground(new java.awt.Color(51, 51, 51));
        myPasswords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_password_25px_2.png"))); // NOI18N
        myPasswords.setText("   My Passwords");
        myPasswords.setContentAreaFilled(false);
        myPasswords.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        myPasswords.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        myPasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myPasswordsActionPerformed(evt);
            }
        });

        dashBoard1.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        dashBoard1.setForeground(new java.awt.Color(51, 51, 51));
        dashBoard1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_Logout_25px.png"))); // NOI18N
        dashBoard1.setText("   Logout");
        dashBoard1.setContentAreaFilled(false);
        dashBoard1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dashBoard1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashBoard1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashBoard1ActionPerformed(evt);
            }
        });

        addnew.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        addnew.setForeground(new java.awt.Color(51, 51, 51));
        addnew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_add_25px.png"))); // NOI18N
        addnew.setText("   Create new");
        addnew.setContentAreaFilled(false);
        addnew.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addnew.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addnew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addnewActionPerformed(evt);
            }
        });

        settings.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        settings.setForeground(new java.awt.Color(51, 51, 51));
        settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_settings_25px.png"))); // NOI18N
        settings.setText("   Settings");
        settings.setContentAreaFilled(false);
        settings.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        settings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsActionPerformed(evt);
            }
        });

        addnew1.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        addnew1.setForeground(new java.awt.Color(51, 51, 51));
        addnew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_note_25px.png"))); // NOI18N
        addnew1.setText("   My Notes");
        addnew1.setContentAreaFilled(false);
        addnew1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addnew1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addnew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addnew1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myPasswords, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
            .addComponent(addnew1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(addnew, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(settings, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dashBoard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(myPasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addnew1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addnew, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                .addComponent(dashBoard1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 210, 720));

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1290, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        jPanel1.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 1290, 720));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void myPasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myPasswordsActionPerformed
        // TODO add your handling code here:
        try
        {

            PasswordList view = new PasswordList ();
            jDesktopPane1.removeAll();
            jDesktopPane1.add(view).setVisible(true);
            myPasswords.setBackground(new Color(19, 141, 117));

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }//GEN-LAST:event_myPasswordsActionPerformed

    private void addnewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addnewActionPerformed
        // TODO add your handling code here:
        try
        {
            Add_new view = new Add_new ();
            jDesktopPane1.removeAll();
            jDesktopPane1.add(view).setVisible(true);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_addnewActionPerformed

    private void dashBoard1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashBoard1ActionPerformed
        // TODO add your handling code here:
        try
        {
            this.setVisible(false);
            Login load = new Login();
            load.setVisible(true);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_dashBoard1ActionPerformed

    private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
        // TODO add your handling code here:
        try
        {
            settings view = new settings ();
            jDesktopPane1.removeAll();
            jDesktopPane1.add(view).setVisible(true);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }//GEN-LAST:event_settingsActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        Profile view = new Profile ();
        jDesktopPane1.removeAll();
        jDesktopPane1.add(view).setVisible(true);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        Profile view = new Profile ();
        jDesktopPane1.removeAll();
        jDesktopPane1.add(view).setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        PasswordList view = new PasswordList ();
        jDesktopPane1.removeAll();
        jDesktopPane1.add(view).setVisible(true);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void addnew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addnew1ActionPerformed
        // TODO add your handling code here:
        NoteList view = new NoteList ();
        jDesktopPane1.removeAll();
        jDesktopPane1.add(view).setVisible(true);
    }//GEN-LAST:event_addnew1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addnew;
    private javax.swing.JButton addnew1;
    private javax.swing.JButton dashBoard1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton myPasswords;
    private javax.swing.JButton settings;
    // End of variables declaration//GEN-END:variables
}
