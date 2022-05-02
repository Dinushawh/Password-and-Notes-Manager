
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

public final class settings extends javax.swing.JInternalFrame {

    /**
     * Creates new form settings
     */
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public static String SECRET_KEY,SALTVALUE,NewSECRET_KEY;
    public static String userid,OldScreateValue,checkID;
      
    
    public settings() {
        
        FlatLightLaf.setup();
        
        initComponents();
        
        UIManager.put( "sTabbedPane.selectedBackground", Color.white );
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI UIE = (BasicInternalFrameUI)this.getUI();
        UIE.setNorthPane(null);
        
        con = Database_connection_CLASS.connection();

        setSecrateValue();          
        checkError.setVisible(false);
        
        checkPassword.setLocationRelativeTo(null);
        changeScreateCode.setLocationRelativeTo(null);
        deleteForever.setLocationRelativeTo(null);

        
    }
    
    
    public void setSecrateValue(){
    
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
                    jLabel4.setText(rsd.getString(10));   
                    settings.OldScreateValue = rsd.getString(10);
                    settings.SECRET_KEY = rsd.getString(10);
                    settings.SALTVALUE = rsd.getString(11);
                    
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
    
    public void setDefaults (){
    
        try{
        
            pw.setText("");
            uid.setText("");
        }
        catch(Exception e){
        
        }
    }
    
    public void checkCredentials(){
        
        try{
            if( uid.getText().equals("")||pw.getText().equals("")  )
            {
                checkError.setVisible(true);checkError.setText("Some fields are missing");
            }
            else
            {
                boolean loopcheck = false;

                stmt = con.createStatement();
                String query = "SELECT * FROM users ";

                String Username = uid.getText();
                String Password = pw.getText();

                ResultSet rs = stmt.executeQuery(query);

                while(rs.next())
                {
                    if ((Username.equals(rs.getString(5)) || Username.equals(rs.getString(6)))  && Password.equals(rs.getString(7)))
                    {

                        if(null == checkID){
                            loopcheck = true;
                            checkPassword.setVisible(false);
                           
                            
                        }
                        else switch (checkID) {
                            case "1":
                                loopcheck = true;
                                checkPassword.setVisible(false);
                                changeScreateCode.setVisible(true);
                                setDefaults();
                                break;

                            default:
                                loopcheck = true;
                                checkPassword.setVisible(false);
                                deleteForever.setVisible(true);
                                setDefaults();
                                break;
                        }
                        
                    }
                }
                if(!loopcheck){
                    checkError.setVisible(true);checkError.setText("Invalid username or password");
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
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
                    
                    if(userid.equals(rsd.getString(2)) && ref.equals(rsd.getString(6))){
                    
                        String EncUsername = rsd.getString(4);
                        String EncPassword = rsd.getString(5);
                        System.out.println(ref+" "+EncUsername+" "+EncPassword);

                        String DecUsername = decrypt(EncUsername);
                        String DecPassword = decrypt(EncPassword);

                        System.out.println(DecUsername+" "+DecPassword);  
                        
                        System.out.println(NewSECRET_KEY); 
                        
                        String DoubleEncUsername = encrypt(DecUsername);
                        String DoubleEncPassword = encrypt(DecPassword);
                        
                        System.out.println(DoubleEncUsername+" "+DoubleEncPassword+"\n");
                        
                        stmt = con.createStatement();
                        String Query2 = "UPDATE `passwords` SET `username_or_email` = ?,`password` = ? WHERE `ref` = ?;";
                        PreparedStatement preparedStmt1 = con.prepareStatement(Query2);
                        preparedStmt1.setString(1, DoubleEncUsername);
                        preparedStmt1.setString(2, DoubleEncPassword);
                        preparedStmt1.setString(3, ref);
                        preparedStmt1.execute();  
                        
                    }
                }      
            }
            updateScreateCode();
            JOptionPane.showMessageDialog (null, "Secrete value updated successfully", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
            UIManager UI=new UIManager();
            UIManager.put("OptionPane.background", Color.white);
            UIManager.put("Panel.background", Color.white);
            jLabel4.setText(NewSECRET_KEY);
            changeScreateCode.setVisible(false);
        }
        catch(SQLException e){
            System.out.println("Error");
        }
    }
    
    public void updateScreateCode(){
    
        try{
            con = Database_connection_CLASS.connection();
            stmt = con.createStatement();
            String Query2 = "UPDATE `users` SET `secret_key` = ? WHERE `id` = ?;";
            PreparedStatement preparedStmt1 = con.prepareStatement(Query2);
            preparedStmt1.setString(1, NewSECRET_KEY);
            preparedStmt1.setString(2, userid);
            preparedStmt1.execute();
            
            
            
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void deleteAllpasswords(){
    
        try{
        
            con = Database_connection_CLASS.connection();
            stmt = con.createStatement();
            
            String Query = "DELETE FROM `notes` WHERE `userid` = ?;";
            PreparedStatement preparedStmt = con.prepareStatement(Query);
            preparedStmt.setString(1, userid);
            preparedStmt.execute();
            
            
            String Query2 = "DELETE FROM `passwords` WHERE `userid` = ?;";
            PreparedStatement preparedStmt1 = con.prepareStatement(Query2);
            preparedStmt1.setString(1, userid);
            preparedStmt1.execute();
            
            
            
            JOptionPane.showMessageDialog (null, "All passwords and notes successfully deleted", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
            UIManager UI=new UIManager();
            UIManager.put("OptionPane.background", Color.white);
            UIManager.put("Panel.background", Color.white);
            
            deleteForever.setVisible(false);
        }
        catch(SQLException e){
        
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

        checkPassword = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        uid = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        checkError = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        pw = new javax.swing.JPasswordField();
        changeScreateCode = new javax.swing.JFrame();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        newSC = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        deleteForever = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        checkPassword.setResizable(false);
        checkPassword.setSize(new java.awt.Dimension(400, 600));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flatIcon2-01.png"))); // NOI18N
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 390, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Please enter username and password to continu this step");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 390, 23));

        jLabel22.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel22.setText("Username or email");
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 270, 39));

        uid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                uidKeyPressed(evt);
            }
        });
        jPanel5.add(uid, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 270, 40));

        jLabel23.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel23.setText("Password");
        jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 270, 39));

        checkError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkError.setText("jLabel24");
        jPanel5.add(checkError, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 390, 24));

        jButton7.setText("Next");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 270, 42));

        jLabel31.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 102));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Forget password?");
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 440, 270, 30));
        jPanel5.add(pw, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 270, 39));

        javax.swing.GroupLayout checkPasswordLayout = new javax.swing.GroupLayout(checkPassword.getContentPane());
        checkPassword.getContentPane().setLayout(checkPasswordLayout);
        checkPasswordLayout.setHorizontalGroup(
            checkPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        checkPasswordLayout.setVerticalGroup(
            checkPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        changeScreateCode.setPreferredSize(new java.awt.Dimension(400, 400));
        changeScreateCode.setSize(new java.awt.Dimension(400, 350));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flatIcon2-01.png"))); // NOI18N

        jLabel25.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Chaange user vault password for better security");

        jLabel26.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel26.setText("New Screate code");

        jButton3.setText("Change Secrete code");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newSC, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel24)
                .addGap(8, 8, 8)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(newSC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout changeScreateCodeLayout = new javax.swing.GroupLayout(changeScreateCode.getContentPane());
        changeScreateCode.getContentPane().setLayout(changeScreateCodeLayout);
        changeScreateCodeLayout.setHorizontalGroup(
            changeScreateCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        changeScreateCodeLayout.setVerticalGroup(
            changeScreateCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        deleteForever.setSize(new java.awt.Dimension(870, 230));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flatIcon2-01.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("All saved passwords and noted will be deleted foreverfrom the server. If you continu this step can not be un done!");

        jButton4.setBackground(new java.awt.Color(255, 0, 51));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Delete forever");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout deleteForeverLayout = new javax.swing.GroupLayout(deleteForever.getContentPane());
        deleteForever.getContentPane().setLayout(deleteForeverLayout);
        deleteForeverLayout.setHorizontalGroup(
            deleteForeverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        deleteForeverLayout.setVerticalGroup(
            deleteForeverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel3.setText("change my vault secret codes");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 14)); // NOI18N
        jLabel4.setText("jLabel4");

        jButton1.setBackground(new java.awt.Color(23, 165, 137));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_edit_20px.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel5.setText("Remove all my vault passwords and notes");

        jButton2.setBackground(new java.awt.Color(23, 165, 137));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_edit_20px.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2))))
                .addContainerGap(877, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(605, Short.MAX_VALUE))
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
        try{
//            setDefaults ();
            checkPassword.setVisible(true);
            uid.setText("");pw.setText("");checkError.setVisible(false);
            settings.checkID = "1";
        }
        catch(Exception e){
        
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void uidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_uidKeyPressed
        // TODO add your handling code here:
        checkError.setVisible(false);
    }//GEN-LAST:event_uidKeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        checkCredentials();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        // TODO add your handling code here:
//        checkPassword.setVisible(false);
//        foegetPassword.setVisible(true);
    }//GEN-LAST:event_jLabel31MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try{
//            setDefaults ();
            checkPassword.setVisible(true);
            uid.setText("");pw.setText("");checkError.setVisible(false);
            settings.checkID = "2";
        }
        catch(Exception e){
        
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try{
        
            if("".equals(newSC.getText())){
            
                JOptionPane.showMessageDialog (null, "Nothing changed", "FAILD!", JOptionPane.INFORMATION_MESSAGE);
                UIManager UI=new UIManager();
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
                
            }
            else {
            
                settings.NewSECRET_KEY = newSC.getText();
                decryptAndEncryptdata();
                
            }
        }
        catch(HeadlessException e){
        
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        deleteAllpasswords();
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame changeScreateCode;
    private javax.swing.JLabel checkError;
    private javax.swing.JFrame checkPassword;
    private javax.swing.JFrame deleteForever;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField newSC;
    private javax.swing.JPasswordField pw;
    private javax.swing.JTextField uid;
    // End of variables declaration//GEN-END:variables
}
