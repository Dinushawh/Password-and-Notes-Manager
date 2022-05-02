import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
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


public final class PasswordDetails extends javax.swing.JLayeredPane {
    
    public static String SECRET_KEY,SALTVALUE,userid,id ;
    public boolean loopCheck = true,defloop = true;
    
    
    public String cryptedd1,cryptedd2,decryptData1,decryptData2,reference;
    
    public String ID2;
    
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
      
    
    public PasswordDetails(String service,String username,String password,String ref) {
        
        FlatLightLaf.setup();
        initComponents();
        
        con = Database_connection_CLASS.connection();
        
        name.setText(service);
        jTextField1.setText(username);
        jPasswordField1.setText(password); 
        getEncryptionDetails();
        
        updateFrame.setLocationRelativeTo(null); deletePassword.setLocationRelativeTo(null); 
        jLabel5.setVisible(false);
        this.cryptedd1 = username;this.cryptedd2 = password;this.reference = ref;
        decrypBtn.setVisible(false);viewPasswordBtn.setVisible(false);update.setVisible(false);deleteBtn.setVisible(false);
        
       
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
                    PasswordDetails.SECRET_KEY = rsd.getString(10);
                    PasswordDetails.SALTVALUE = rsd.getString(11);
                }
            }
            
            if(!loopcheck){
                System.out.println("Error setting screte value");
            }
        
        }
        catch(SQLException e){
        
        }
    }

    
    public void optionLoop (){
    
        try{

            
            if (defloop == true){
            
                this.defloop = false;
                decrypBtn.setVisible(true);viewPasswordBtn.setVisible(true);update.setVisible(true);deleteBtn.setVisible(true);
                
            }
            else{
            
                this.defloop = true;
                decrypBtn.setVisible(false);viewPasswordBtn.setVisible(false);update.setVisible(false);deleteBtn.setVisible(false);

            }

        
        }
        catch(Exception e){
        
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
    
    public void decryptData(){
    
        try{

            String value2 = jTextField1.getText();
            String decryptedval2 = decrypt(value2); 
            jTextField1.setText(decryptedval2);
            
            String value3 = jPasswordField1.getText();
            String decryptedval3 = decrypt(value3);
            jPasswordField1.setText(decryptedval3);
        
        }
        catch(Exception e){
            System.out.println("Error Decryptiong data");
        }
    }
    
    public void changesCheck(){
        try{
            
            if("".equals(newUsername.getText()) || "".equals(newPassword.getText()) || "".equals(confirmPassword.getText())){
            
                jLabel5.setVisible(true);jLabel5.setText("There is some feilds are missing");
            }
            else{
            
                if(newUsername.getText().equals(decryptData1) && newPassword.getText().equals(decryptData2) && confirmPassword.getText().equals(decryptData2) ){

                    jLabel5.setVisible(true);jLabel5.setText("Nothing changed");
            
                }
                else{
                    if(newPassword.getText().equals(confirmPassword.getText())){
                        
                        updateData();
                    }
                    else{

                        jLabel5.setVisible(true);jLabel5.setText("Passwords doenst match");
                    }
                }
            }
           
        }
        catch(Exception e){
            System.out.println("error checking changes");
        }
    }

    public void updateData(){
    
        try{
            
            newUsername.getText();
            newPassword.getText();
            
            String encryptedval1 = encrypt(newUsername.getText());
            String encryptedval2 = encrypt(newPassword.getText());
            String Query = "UPDATE `passwords` SET `username_or_email` = ?, `password` = ? WHERE `ref` = ?;"; 
            PreparedStatement preparedStmt = con.prepareStatement(Query);
            preparedStmt.setString(1, encryptedval1);
            preparedStmt.setString(2, encryptedval2);
            preparedStmt.setString(3, reference);
            preparedStmt.execute();
            
            JOptionPane.showMessageDialog (null, "Details Successfully updated! Please reload the page after action complete", "SUCESS!", JOptionPane.INFORMATION_MESSAGE);
            UIManager UI=new UIManager();
            UIManager.put("OptionPane.background", Color.white);
            UIManager.put("Panel.background", Color.white);
            
            updateFrame.setVisible(false);
            PasswordList abc = new PasswordList();
            
            

        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public void DeleteData(){
        try{
        
            stmt = con.createStatement();
            String Query3 = "DELETE FROM `passwords` WHERE `ref` = ?;";
            PreparedStatement preparedStmt2 = con.prepareStatement(Query3);
            preparedStmt2.setString(1, reference);
            preparedStmt2.execute();

            deletePassword.setVisible(false);
            JOptionPane.showMessageDialog (null, "Details Successfully Deleted! Please reload the page after action complete", "SUCESS!", JOptionPane.INFORMATION_MESSAGE);
            UIManager UI=new UIManager();
            UIManager.put("OptionPane.background", Color.white);
            UIManager.put("Panel.background", Color.white);
            
            updateFrame.setVisible(false);
            PasswordList abc = new PasswordList();
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

        updateFrame = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        dashBoard3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        newUsername = new javax.swing.JTextField();
        newPassword = new javax.swing.JPasswordField();
        confirmPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        updateData = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        deletePassword = new javax.swing.JFrame();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        deletedata = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        viewPasswordBtn = new javax.swing.JButton();
        decrypBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        updateFrame.setTitle("Update Password");
        updateFrame.setResizable(false);
        updateFrame.setSize(new java.awt.Dimension(400, 600));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 600));

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

        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel1.setText("New password");

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Update Password Details");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel3.setText("New username");

        newUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUsernameActionPerformed(evt);
            }
        });
        newUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newUsernameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newUsernameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newUsernameKeyTyped(evt);
            }
        });

        newPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newPasswordKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newPasswordKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newPasswordKeyTyped(evt);
            }
        });

        confirmPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                confirmPasswordKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmPasswordKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                confirmPasswordKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel4.setText("Confirm new pssword");

        updateData.setText("Save");
        updateData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDataActionPerformed(evt);
            }
        });

        jLabel5.setText("jLabel5");

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flatIcon2-01.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(260, 260, 260)
                                        .addComponent(dashBoard3)))
                                .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(confirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(updateData, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dashBoard3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(confirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(updateData, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout updateFrameLayout = new javax.swing.GroupLayout(updateFrame.getContentPane());
        updateFrame.getContentPane().setLayout(updateFrameLayout);
        updateFrameLayout.setHorizontalGroup(
            updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );
        updateFrameLayout.setVerticalGroup(
            updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        deletePassword.setTitle("Delete Permently");
        deletePassword.setResizable(false);
        deletePassword.setSize(new java.awt.Dimension(800, 200));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel6.setText("This action cannot be undone!");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semilight", 0, 16)); // NOI18N
        jLabel7.setText("This password details will be permenently delete from the database!");

        deletedata.setBackground(new java.awt.Color(255, 0, 51));
        deletedata.setForeground(new java.awt.Color(255, 255, 255));
        deletedata.setText("Delete");
        deletedata.setPreferredSize(new java.awt.Dimension(63, 30));
        deletedata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletedataActionPerformed(evt);
            }
        });

        jButton5.setText("Cansel");
        jButton5.setPreferredSize(new java.awt.Dimension(65, 30));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deletedata, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deletedata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout deletePasswordLayout = new javax.swing.GroupLayout(deletePassword.getContentPane());
        deletePassword.getContentPane().setLayout(deletePasswordLayout);
        deletePasswordLayout.setHorizontalGroup(
            deletePasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        deletePasswordLayout.setVerticalGroup(
            deletePasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPopupMenu1.setToolTipText("Option");

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));

        name.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        name.setText("Dinusha Weerakoon");

        jTextField1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        jTextField1.setText("jTextField1");
        jTextField1.setBorder(null);
        jTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jPasswordField1.setFont(new java.awt.Font("Segoe UI Semilight", 0, 15)); // NOI18N
        jPasswordField1.setText("jPasswordField1");
        jPasswordField1.setBorder(null);
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        viewPasswordBtn.setBackground(new java.awt.Color(53, 126, 199));
        viewPasswordBtn.setForeground(new java.awt.Color(255, 255, 255));
        viewPasswordBtn.setText("View Passwword");
        viewPasswordBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPasswordBtnActionPerformed(evt);
            }
        });

        decrypBtn.setBackground(new java.awt.Color(40, 180, 99));
        decrypBtn.setForeground(new java.awt.Color(255, 255, 255));
        decrypBtn.setText("Decrypt");
        decrypBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decrypBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(221, 52, 57));
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        update.setBackground(new java.awt.Color(20, 29, 39));
        update.setForeground(new java.awt.Color(255, 255, 255));
        update.setText("update");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_bulleted_list_20px.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(decrypBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(viewPasswordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 473, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(decrypBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewPasswordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        // TODO add your handling code here:
        try{
            
             updateFrame.setVisible(true);
             this.decryptData1 = decrypt(cryptedd1); this.decryptData2 = decrypt(cryptedd2); 
             newUsername.setText(decryptData1);
             newPassword.setText(decryptData2);
             System.out.println(decryptData1 + " " +decryptData2);   

        }
        catch(Exception e){
            System.out.println("cannot unhide");
        }

    }//GEN-LAST:event_updateActionPerformed

    private void decrypBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decrypBtnActionPerformed
        // TODO add your handling code here:
        try{
            decryptData();
            decrypBtn.setEnabled(false);
        }
        catch(Exception e){
            System.out.println("error");
        }
    }//GEN-LAST:event_decrypBtnActionPerformed

    private void viewPasswordBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPasswordBtnActionPerformed
        // TODO add your handling code here:
        jPasswordField1.setEchoChar((char)0);
    }//GEN-LAST:event_viewPasswordBtnActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        try{
//            jFrame1.setVisible(true);
//            PasswordList ab = new PasswordList();
//            ab.reference = reference;
//            ab.fjewbf.setVisible(true);
//            
            deletePassword.setVisible(true);
            
        }
        catch(Exception e){
        }

    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updateDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDataActionPerformed
        // TODO add your handling code here:
        try{
             changesCheck();
             System.out.println(reference);
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }//GEN-LAST:event_updateDataActionPerformed

    private void newUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newUsernameKeyPressed
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_newUsernameKeyPressed

    private void newUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newUsernameKeyReleased
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_newUsernameKeyReleased

    private void newUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newUsernameKeyTyped
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_newUsernameKeyTyped

    private void newPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPasswordKeyPressed
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_newPasswordKeyPressed

    private void newPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPasswordKeyReleased
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_newPasswordKeyReleased

    private void newPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPasswordKeyTyped
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_newPasswordKeyTyped

    private void confirmPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordKeyPressed
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_confirmPasswordKeyPressed

    private void confirmPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordKeyReleased
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_confirmPasswordKeyReleased

    private void confirmPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordKeyTyped
        // TODO add your handling code here:
        try{
            jLabel5.setVisible(false);
        }
        catch(Exception e){}
    }//GEN-LAST:event_confirmPasswordKeyTyped

    private void newUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newUsernameActionPerformed

    private void dashBoard3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashBoard3ActionPerformed
        // TODO add your handling code here:
        try{
            
            if (loopCheck == true){
            
                this.loopCheck = false;
                dashBoard3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_invisible_20px.png")));
                newPassword.setEchoChar((char)0);confirmPassword.setEchoChar((char)0);
            }
            else{
            
                this.loopCheck = true;
                dashBoard3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8_eye_20px.png")));
                newPassword.setEchoChar('*');confirmPassword.setEchoChar('*');
            }
            
        
        }
        catch(Exception e ){}
        
        

    }//GEN-LAST:event_dashBoard3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try{
            deletePassword.setVisible(false);
        }
        catch(Exception e){
        
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void deletedataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletedataActionPerformed
        // TODO add your handling code here:
        try{
        
            DeleteData();
        }
        catch(Exception e){
        
        }
    }//GEN-LAST:event_deletedataActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        optionLoop ();
    }//GEN-LAST:event_jLabel8MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField confirmPassword;
    private javax.swing.JButton dashBoard3;
    private javax.swing.JButton decrypBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JFrame deletePassword;
    private javax.swing.JButton deletedata;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel name;
    private javax.swing.JPasswordField newPassword;
    private javax.swing.JTextField newUsername;
    private javax.swing.JButton update;
    private javax.swing.JButton updateData;
    private javax.swing.JFrame updateFrame;
    private javax.swing.JButton viewPasswordBtn;
    // End of variables declaration//GEN-END:variables
}
