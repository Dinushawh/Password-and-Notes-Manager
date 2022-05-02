
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
import javax.swing.plaf.basic.BasicInternalFrameUI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dinus
 */
public final class Add_new_note extends javax.swing.JInternalFrame {

    /**
     * Creates new form Addnewnote
     */
    
    public static String SECRET_KEY,SALTVALUE,userid,ref ;
    
    public boolean loopCheck = true;
    
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public Add_new_note() {
        initComponents();
        
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI UIE = (BasicInternalFrameUI)this.getUI();
        UIE.setNorthPane(null);
        con = Database_connection_CLASS.connection();
        genarateRef();
        getEncryptionDetails();
        
        System.out.println(userid);
            
        
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
                    Add_new_note.SECRET_KEY = rsd.getString(10);
                    Add_new_note.SALTVALUE = rsd.getString(11);
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
            ResultSet rsd = stmt.executeQuery("SELECT MAX(ref) FROM notes");
            rsd.next();
            rsd.getString("MAX(ref)");
            if (rsd.getString("MAX(ref)") == null) {
                itemidset = "N000001";
                Add_new_note.ref = itemidset;
                System.out.println(ref);

            } else {
                long id = Long.parseLong(rsd.getString("MAX(ref)").substring(3, rsd.getString("MAX(ref)").length()));
                id++;
                itemidset = ("N" + String.format("%06d", id));
                Add_new_note.ref = itemidset;
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
    
    public void clearFeilds (){
    
        try{
        
            subject.setText("");note.setText("");
        }
        catch(Exception e){
        
        }
    }
    
    public void checkAndEncrypt() throws SQLException{
    
        try{
        
            if("".equals(note.getText()) || "".equals(subject.getText())){
            
                JOptionPane.showMessageDialog (null, "Empty Feilds", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
            }
            else{
            
                String SUBJECT = subject.getText();
                String EncryptedSubject = encrypt(SUBJECT);
                
                String NOTE = note.getText();
                String EncryptedNote = encrypt(NOTE);  
                
                System.out.println(EncryptedSubject+" "+EncryptedNote);
                
                stmt = con.createStatement();
                String query2 = "INSERT INTO `notes`(`userid`, `subject`, `note`, `ref`) VALUES (?,?,?,?)";
                PreparedStatement preparedStmt = con.prepareStatement(query2);
                preparedStmt.setString(1, userid);
                preparedStmt.setString(2, EncryptedSubject);
                preparedStmt.setString(3, EncryptedNote);
                preparedStmt.setString(4, ref);
                preparedStmt.execute();

                JOptionPane.showMessageDialog (null, "New password succrssfully added to the system", "success!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
                
                clearFeilds();
                genarateRef();
            }
            
            
        }
        catch(HeadlessException e){
        
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
        subject = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        note = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel1.setText("Add new note");

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel2.setText("Subject");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel3.setText("Subject");

        note.setColumns(20);
        note.setRows(5);
        jScrollPane1.setViewportView(note);

        jButton1.setText("Add Note");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(888, 888, 888))
                        .addComponent(subject)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1197, Short.MAX_VALUE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        String test = note.getText();
//        System.out.println(test);
        try {
            checkAndEncrypt();
        } catch (SQLException ex) {
            Logger.getLogger(Add_new_note.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea note;
    private javax.swing.JTextField subject;
    // End of variables declaration//GEN-END:variables
}
