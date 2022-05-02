
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.HeadlessException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dinus
 */


public final class Add_new_password extends javax.swing.JInternalFrame {

    
    /**
     * Creates new form Add_new_password
     */
    
    public static String SECRET_KEY ;
    public static String SALTVALUE ;
    public static String userid,ref;
    
    public boolean loopCheck = true;
    
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
      
    public Add_new_password() {
        FlatLightLaf.setup();
        initComponents();
        
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI UIE = (BasicInternalFrameUI)this.getUI();
        UIE.setNorthPane(null);
        
        System.out.println(SECRET_KEY+SALTVALUE);
        
        con = Database_connection_CLASS.connection();
        genarateRef();
        getEncryptionDetails();
            
        
    }
    
    public void getEncryptionDetails(){
    
        try{
            
            boolean loopcheck = false;
            stmt = con.createStatement();
            String query = "SELECT * FROM users ";
            ResultSet rsd = stmt.executeQuery(query);

            while(rsd.next())
            {
                if (userid.equals(rsd.getString(2)))
                {
                    loopcheck = true;
                    Add_new_password.SECRET_KEY = rsd.getString(10);
                    Add_new_password.SALTVALUE = rsd.getString(11);
                }
            }
            
            if(!loopcheck){
                System.out.println("Error setting screte value");
            }
        
        }
        catch(SQLException e){
        
        }
    }
    
    public void genarateRef(){
    
        try 
        {
            String itemidset = null;
            stmt = con.createStatement();
            ResultSet rsd = stmt.executeQuery("SELECT MAX(ref) FROM passwords");
            rsd.next();
            rsd.getString("MAX(ref)");
            if (rsd.getString("MAX(ref)") == null) {
                itemidset = "REF000001";
                Add_new_password.ref = itemidset;
                System.out.println(ref);

            } else {
                long id = Long.parseLong(rsd.getString("MAX(ref)").substring(3, rsd.getString("MAX(ref)").length()));
                id++;
                itemidset = ("REF" + String.format("%06d", id));
                Add_new_password.ref = itemidset;
                System.out.println(ref);
                }
            } 
        catch (NumberFormatException | SQLException e) {
            System.out.println(e);
        }
    }
    
    public static String encrypt(String strToEncrypt){  
        try   
        {  
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
            IvParameterSpec ivspec = new IvParameterSpec(iv);        
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);  
            SecretKey tmp = factory.generateSecret(spec);  
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);   
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));  
        }   
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)   
        {  
          System.out.println("Error occured during encryption: " + e.toString());  
        }  
        return null;  
    }  
    

    public void clearFeilds(){
    
        try{
            SERVICE.setText("");USERNAME.setText("");PASSWORD1.setText("");PASSWORD2.setText("");
        
        }
        catch(Exception e){
            System.out.println("Error clear feilds");
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        USERNAME = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        SERVICE = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        PASSWORD2 = new javax.swing.JPasswordField();
        PASSWORD1 = new javax.swing.JPasswordField();
        button1 = new Button();
        dashBoard3 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel1.setText("Add new account");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setText("Confirm Password");

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setText("Name / Service / Domain");

        SERVICE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SERVICEActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Username or Email");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Password");

        button1.setBackground(new java.awt.Color(0, 153, 153));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Add now");
        button1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        dashBoard3.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        dashBoard3.setForeground(new java.awt.Color(51, 51, 51));
        dashBoard3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_eye_20px.png"))); // NOI18N
        dashBoard3.setContentAreaFilled(false);
        dashBoard3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dashBoard3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashBoard3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashBoard3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PASSWORD1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dashBoard3))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                        .addComponent(USERNAME, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SERVICE, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(button1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                        .addComponent(PASSWORD2, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(697, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SERVICE, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(USERNAME, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dashBoard3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PASSWORD1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PASSWORD2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );

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
    }// </editor-fold>//GEN-END:initComponents

    private void SERVICEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SERVICEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SERVICEActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        try{
            if(PASSWORD1.getText().equals(PASSWORD2.getText())){
                
                
                String dbService = SERVICE.getText();
                
                String value2 = USERNAME.getText();
                String EncryptedUsername = encrypt(value2);  

                String value3 = PASSWORD1.getText();
                String EncryptedPassword = encrypt(value3);  

                System.out.println(EncryptedUsername + " "+EncryptedPassword );
                
                System.out.println(userid+" "+dbService+ " "+EncryptedUsername+" "+EncryptedPassword+" "+ref);
                
                stmt = con.createStatement();
                String query2 = "INSERT INTO `passwords`(`userid`, `service`, `username_or_email`, `password`,`ref`) VALUES (?,?,?,?,?)";
                PreparedStatement preparedStmt = con.prepareStatement(query2);
                preparedStmt.setString(1, userid);
                preparedStmt.setString(2, dbService);
                preparedStmt.setString(3, EncryptedUsername);
                preparedStmt.setString(4, EncryptedPassword);
                preparedStmt.setString(5, ref);
                preparedStmt.execute();

                JOptionPane.showMessageDialog (null, "New password succrssfully added to the system", "success!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
                
                clearFeilds();
                genarateRef();
                
            }
            else{
            
                JOptionPane.showMessageDialog (null, "Password Does not match", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
            }            
        }
        catch(HeadlessException e){
            System.out.println(e);
        } catch (SQLException ex) {
            Logger.getLogger(Add_new_password.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_button1ActionPerformed

    private void dashBoard3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashBoard3ActionPerformed
        // TODO add your handling code here:
        try{

            if (loopCheck == true){

                this.loopCheck = false;
                dashBoard3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_invisible_20px.png")));
                PASSWORD1.setEchoChar((char)0);PASSWORD2.setEchoChar((char)0);
            }
            else{

                this.loopCheck = true;
                dashBoard3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_eye_20px.png")));
                PASSWORD1.setEchoChar('*');PASSWORD2.setEchoChar('*');
            }

        }
        catch(Exception e ){}

    }//GEN-LAST:event_dashBoard3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField PASSWORD1;
    private javax.swing.JPasswordField PASSWORD2;
    private javax.swing.JTextField SERVICE;
    private javax.swing.JTextField USERNAME;
    private Button button1;
    private javax.swing.JButton dashBoard3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
