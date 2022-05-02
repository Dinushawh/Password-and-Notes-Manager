import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.HeadlessException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
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



public final class Changerusername extends javax.swing.JLayeredPane {
    
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public static String SECRET_KEY,SALTVALUE,NewSECRET_KEY;
    public static String userid,OldScreateValue;
    
    public Changerusername(String service,String username,String password) {
        
        FlatLightLaf.setup();
        initComponents();   
        
        setSecrateValue();
    }
    
    public void setSecrateValue(){
    
        try{
            boolean loopcheck = false;
            stmt = con.createStatement();
            String query = "SELECT * FROM users ";
            ResultSet rsd = stmt.executeQuery(query);

            while(rsd.next())
            {
                if (userid.equals(rsd.getString(6)))
                {
                    loopcheck = true;
                    jTextField1.setText(rsd.getString(10));
                    settings.OldScreateValue = rsd.getString(10);
                    settings.SECRET_KEY = rsd.getString(10);
                }
            }
            
            if(!loopcheck){
                System.out.println("Error setting screte value");
            }
        
        }
        catch(SQLException e){
            System.out.println("Error setting screte value");
        }
    }
    
    public static String decrypt(String strToDecrypt){  
        try   
        {  
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
            IvParameterSpec ivspec = new IvParameterSpec(iv);  
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);  
            SecretKey tmp = factory.generateSecret(spec);  
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");  
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);  
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));  
        }   
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)   
        {  
          System.out.println("Error occured during decryption: " + e.toString());  
        }  
        return null;  
    }
    
    public static String encrypt(String strToEncrypt){  
        try   
        {  
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
            IvParameterSpec ivspec = new IvParameterSpec(iv);        
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");  
            KeySpec spec = new PBEKeySpec(NewSECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);  
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
    
    public void decryptAndEncryptdata(){
    
        try{
            
            con = Database_connection_CLASS.connection();
            stmt = con.createStatement();
            String query = "SELECT * FROM passwords ";
            ResultSet rsd = stmt.executeQuery(query);

            while(rsd.next())
            {
                if(userid.equals(rsd.getString(2))){
                    
                    String ref = rsd.getString(6);
                    
                    String value1 = rsd.getString(4);
                    String value2 = rsd.getString(5);
                    System.out.println(ref+""+value1+" "+value2);
                    
                    String dv1 = decrypt(value1);
                    String dv2 = decrypt(value2);
                    System.out.println(dv1+" "+dv2);
                    
                    System.out.println(NewSECRET_KEY);
                    
                    String encval1 = encrypt(dv1);
                    String encval2 = encrypt(dv2);
                    
                    System.out.println(encval1+" "+encval2);
                    
                    stmt = con.createStatement();
                    String Query2 = "UPDATE `passwords` SET `username_or_email` = ?,`password` = ? WHERE `ref` = ?;";
                    PreparedStatement preparedStmt1 = con.prepareStatement(Query2);
                    preparedStmt1.setString(1, encval1);
                    preparedStmt1.setString(2, encval2);
                    preparedStmt1.setString(3, ref);
                    preparedStmt1.execute();   
                }      
            }
            System.out.println("outter layer");
            updateScreateCode();
        }
        catch(SQLException e){
            System.out.println("Error");
        }
    }
    
    public void updateScreateCode(){
    
        try{
            con = Database_connection_CLASS.connection();
            stmt = con.createStatement();
            String Query2 = "UPDATE `users` SET `secret_key` = ? WHERE `username` = ?;";
            PreparedStatement preparedStmt1 = con.prepareStatement(Query2);
            preparedStmt1.setString(1, NewSECRET_KEY);
            preparedStmt1.setString(2, userid);
            preparedStmt1.execute();
        }
        catch(SQLException e){
            System.out.println(e);
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
        jTextField1 = new javax.swing.JTextField();
        changeSC = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        changeSC.setText("Change");
        changeSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSCActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        jLabel1.setText("Change Secrete code");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(changeSC, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(678, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(changeSC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(598, Short.MAX_VALUE))
        );

        setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        try{

        }
        catch(Exception e){
        
        }
        
    }//GEN-LAST:event_formMouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void changeSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSCActionPerformed
        // TODO add your handling code here:
        try{

            if(SECRET_KEY.equals(jTextField1.getText())){
                JOptionPane.showMessageDialog (null, "Nothing changed", "FAILD!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
            }
            else{
                settings.NewSECRET_KEY = jTextField1.getText();
                decryptAndEncryptdata();
                setSecrateValue();
                JOptionPane.showMessageDialog (null, "Successfully changed the screate code", "SUCESS!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
            }
        }
        catch(HeadlessException e){
            System.out.println(e);
        }
    }//GEN-LAST:event_changeSCActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeSC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
