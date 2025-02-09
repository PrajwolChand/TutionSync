/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fees_management_system;
import  java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import javax.swing.JTable;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import static java.lang.Integer.parseInt;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author prajwol
 */
public class PrintReciept1 extends javax.swing.JFrame {

    /**
     * Creates new form PrintReciept
     */
    
    public PrintReciept1() {
       
       
      // jPanel1=new JPanel();
      //  jLabel22= new JLabel();
        
            //jLabel22.setText("hello");
        
        initComponents();
        getRecords();
          int accountantId = extractAccountantId();
if (accountantId != -1) {
    //JOptionPane.showMessageDialog(this, "Accountant is currently active.");
} else {
    JOptionPane.showMessageDialog(this, "No accountant is currently active.");
}
setAccountantName(accountantId); // Set the accountant name label
        
    }
    
   
    
    public void getRecords () {

try{

Connection con = DBConnection.getConnection();

PreparedStatement pst = con.prepareStatement("select * from fees_details_dd order by reciept_no desc fetch first 1 rows only"); 
ResultSet rs = pst.executeQuery();

rs.next();

txt_receiptNo.setText(rs.getString("reciept_no")); 
txt_paymentMode.setText(rs.getString("payment_mode"));


String paymentMode = rs.getString("payment_mode");

if (paymentMode.equalsIgnoreCase("cheque")){ 
    jLabel9.setText("Cheque No       :");
     txt_cheque_esewa.setText(rs.getString("cheque_no"));
     txt_bankName.setText(rs.getString("bank_name"));

 }
if (paymentMode.equalsIgnoreCase("cash")){ 
    jLabel9.setText("Cheque No       :");
     txt_cheque_esewa.setText("-");
     txt_bankName.setText("-");

 }
if (paymentMode.equalsIgnoreCase("e-sewa")){ 
    jLabel9.setText("E-sewa No              :");
     txt_cheque_esewa.setText(rs.getString("esewa_no"));
     jLabel14.setText("Transaction Code   :");
     txt_bankName.setText(rs.getString("transaction_code"));
    

 }
if (paymentMode.equalsIgnoreCase("card")){ 
    jLabel9.setText("Cheque No       :");
     txt_cheque_esewa.setText("-");
     txt_bankName.setText(rs.getString("bank_name"));

 }
txt_receivedFrom.setText(rs.getString("student_name"));
txt_date.setText(rs.getString("date"));

txt_amount1.setText(rs.getString("amount"));
txt_total.setText(rs.getString("total_amount"));
txt_totalInWords.setText(rs.getString("total_in_words"));
txt_remark.setText(rs.getString("remark"));
txt_courseName.setText(rs.getString("course_name"));

}catch (Exception e) {
e.printStackTrace();
        }

}
   
    
    
  int extractAccountantId() {
    int accountantId = -1; // Initialize with a default value
    try {
        // Load the JDBC driver
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        
        // Establish a connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/fees_management", "root", "root");
        
        // Prepare the SQL SELECT statement to get the latest accountant ID
        String sql = "SELECT id FROM active_accountant ORDER BY id DESC FETCH FIRST 1 ROWS ONLY";
        PreparedStatement pst = con.prepareStatement(sql);
        
        // Execute the query
        ResultSet rs = pst.executeQuery();
        
        // Check if a result exists and retrieve the latest ID
        if (rs.next()) {
            accountantId = rs.getInt("id"); // Extract the latest ID
        } else {
            JOptionPane.showMessageDialog(this, "No active accountant found.");
        }
        
        // Close the connection
        con.close();
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    
    return accountantId; // Return the latest accountant ID
}

void setAccountantName(int accountantId) {
    try {
        // Load the JDBC driver
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        // Establish a connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/fees_management", "root", "root");

        // Prepare the SQL SELECT statement to get the accountant_name based on accountantId
        String sql = "SELECT accountant_name FROM accountant_details WHERE accountant_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);

        // Set the accountantId in the prepared statement
        pst.setInt(1, accountantId);

        // Execute the query
        ResultSet rs = pst.executeQuery();

        // Check if a result exists and retrieve the accountant name
        if (rs.next()) {
    String accountantNameStr = rs.getString("accountant_name"); // Extract the accountant name
    System.out.println(accountantNameStr);

    // Ensure jLabel22 is initialized and added to the panel in the constructor
    jLabel22.setText(accountantNameStr); // Set the text

    // Update the UI
    //jPanel1.revalidate(); // Revalidate the panel to update layout
    //jPanel1.repaint(); // Repaint the panel to reflect changes
} else {
    JOptionPane.showMessageDialog(this, "No accountant found with the given ID.");
}

        // Close the connection
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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

        panelsideBar = new javax.swing.JPanel();
        panelHome = new javax.swing.JPanel();
        btnHome = new javax.swing.JLabel();
        panelSearch = new javax.swing.JPanel();
        btnSearch = new javax.swing.JLabel();
        panelEdit = new javax.swing.JPanel();
        btnEdit = new javax.swing.JLabel();
        panelCourseList = new javax.swing.JPanel();
        btnCourseList = new javax.swing.JLabel();
        panelViewAllRecord = new javax.swing.JPanel();
        btnViewAllRecord = new javax.swing.JLabel();
        panelViewAllRecord1 = new javax.swing.JPanel();
        btnViewAllRecord1 = new javax.swing.JLabel();
        panelLogout = new javax.swing.JPanel();
        btnLogout = new javax.swing.JLabel();
        panelBack1 = new javax.swing.JPanel();
        btn_print = new javax.swing.JLabel();
        panelBack2 = new javax.swing.JPanel();
        btn_edit = new javax.swing.JLabel();
        panelEditStudent = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txt_paymentMode = new javax.swing.JLabel();
        txt_cheque_esewa = new javax.swing.JLabel();
        txt_bankName = new javax.swing.JLabel();
        txt_receivedFrom = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_receiptNo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_date = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        txt_total = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_totalInWords = new javax.swing.JLabel();
        txt_courseName = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        txt_amount1 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txt_remark = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelsideBar.setBackground(new java.awt.Color(124, 16, 52));
        panelsideBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHome.setBackground(new java.awt.Color(124, 16, 52));
        panelHome.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelHome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnHome.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/home.png"))); // NOI18N
        btnHome.setText("   HOME");
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });
        panelHome.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 50));

        panelsideBar.add(panelHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 270, 50));

        panelSearch.setBackground(new java.awt.Color(124, 16, 52));
        panelSearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelSearch.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSearch.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/search2.png"))); // NOI18N
        btnSearch.setText("   Search Records");
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchMouseExited(evt);
            }
        });
        panelSearch.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 230, 50));

        panelsideBar.add(panelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 270, 50));

        panelEdit.setBackground(new java.awt.Color(124, 16, 52));
        panelEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelEdit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEdit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/edit2.png"))); // NOI18N
        btnEdit.setText("   Edit Courses");
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMouseExited(evt);
            }
        });
        panelEdit.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 50));

        panelsideBar.add(panelEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 270, 50));

        panelCourseList.setBackground(new java.awt.Color(124, 16, 52));
        panelCourseList.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelCourseList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCourseList.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnCourseList.setForeground(new java.awt.Color(255, 255, 255));
        btnCourseList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/list_1.png"))); // NOI18N
        btnCourseList.setText("   Course List");
        btnCourseList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCourseListMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCourseListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCourseListMouseExited(evt);
            }
        });
        panelCourseList.add(btnCourseList, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 60));

        panelsideBar.add(panelCourseList, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 270, 50));

        panelViewAllRecord.setBackground(new java.awt.Color(124, 16, 52));
        panelViewAllRecord.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelViewAllRecord.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnViewAllRecord.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnViewAllRecord.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAllRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/view all record.png"))); // NOI18N
        btnViewAllRecord.setText("   View All Record");
        btnViewAllRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnViewAllRecordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewAllRecordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewAllRecordMouseExited(evt);
            }
        });
        panelViewAllRecord.add(btnViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 230, 50));

        panelViewAllRecord1.setBackground(new java.awt.Color(124, 16, 52));
        panelViewAllRecord1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelViewAllRecord1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnViewAllRecord1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnViewAllRecord1.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAllRecord1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/view all record.png"))); // NOI18N
        btnViewAllRecord1.setText("   View All Record");
        btnViewAllRecord1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewAllRecord1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewAllRecord1MouseExited(evt);
            }
        });
        panelViewAllRecord1.add(btnViewAllRecord1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 230, 50));

        panelViewAllRecord.add(panelViewAllRecord1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 270, 50));

        panelsideBar.add(panelViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 270, 50));

        panelLogout.setBackground(new java.awt.Color(124, 16, 52));
        panelLogout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelLogout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogout.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/logout.png"))); // NOI18N
        btnLogout.setText("   Logout");
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });
        panelLogout.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 50));

        panelsideBar.add(panelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 270, 50));

        panelBack1.setBackground(new java.awt.Color(124, 16, 52));
        panelBack1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelBack1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_print.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btn_print.setForeground(new java.awt.Color(255, 255, 255));
        btn_print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/printer-.png"))); // NOI18N
        btn_print.setText("  Print");
        btn_print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_printMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_printMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_printMouseExited(evt);
            }
        });
        panelBack1.add(btn_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 50));

        panelsideBar.add(panelBack1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, 270, 50));

        panelBack2.setBackground(new java.awt.Color(124, 16, 52));
        panelBack2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelBack2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_edit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btn_edit.setForeground(new java.awt.Color(255, 255, 255));
        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/edit2.png"))); // NOI18N
        btn_edit.setText("  Edit");
        btn_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_editMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_editMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_editMouseExited(evt);
            }
        });
        panelBack2.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 50));

        panelsideBar.add(panelBack2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 270, 50));

        panelEditStudent.setBackground(new java.awt.Color(124, 16, 52));
        panelEditStudent.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), java.awt.Color.white, null, null));
        panelEditStudent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/student.png"))); // NOI18N
        jLabel2.setText("   Edit Student");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });
        panelEditStudent.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 50));

        panelsideBar.add(panelEditStudent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 270, 50));

        getContentPane().add(panelsideBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 650));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(124, 16, 52));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/swastik-modified (3).png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 100, 100));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(124, 16, 52));
        jLabel12.setText("Charodobato, Bhaktapur");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(124, 16, 52));
        jLabel7.setText("Swastik IT Institute");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, -1, -1));

        jPanel2.setBackground(new java.awt.Color(124, 16, 52));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 900, 5));

        txt_paymentMode.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_paymentMode.setForeground(new java.awt.Color(124, 16, 52));
        txt_paymentMode.setText("Payment Mode  ");
        jPanel1.add(txt_paymentMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 140, -1));

        txt_cheque_esewa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_cheque_esewa.setForeground(new java.awt.Color(124, 16, 52));
        txt_cheque_esewa.setText("cheque_esewa ");
        jPanel1.add(txt_cheque_esewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 130, -1));

        txt_bankName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_bankName.setForeground(new java.awt.Color(124, 16, 52));
        txt_bankName.setText("Bank Name  ");
        jPanel1.add(txt_bankName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 170, -1));

        txt_receivedFrom.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_receivedFrom.setForeground(new java.awt.Color(124, 16, 52));
        txt_receivedFrom.setText("received from");
        jPanel1.add(txt_receivedFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 220, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(124, 16, 52));
        jLabel8.setText("SOC-");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, -1, -1));

        txt_receiptNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_receiptNo.setForeground(new java.awt.Color(124, 16, 52));
        txt_receiptNo.setText("Receipt No  ");
        jPanel1.add(txt_receiptNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, 130, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(124, 16, 52));
        jLabel11.setText("Amount");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 300, 60, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(124, 16, 52));
        jLabel9.setText("Cheque_esewa       :");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(124, 16, 52));
        jLabel13.setText("Receipt No              :");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(124, 16, 52));
        jLabel10.setText("Payment Mode       :");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(124, 16, 52));
        jLabel14.setText("Bank Name             :");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        txt_date.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_date.setForeground(new java.awt.Color(124, 16, 52));
        txt_date.setText("Date  ");
        jPanel1.add(txt_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 140, 150, -1));

        jSeparator2.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator2.setForeground(new java.awt.Color(124, 16, 52));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 890, 10));

        jSeparator3.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator3.setForeground(new java.awt.Color(124, 16, 52));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, 170, 10));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(124, 16, 52));
        jLabel15.setText("Received From       :");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        txt_total.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_total.setForeground(new java.awt.Color(124, 16, 52));
        txt_total.setText("Total");
        jPanel1.add(txt_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 420, 180, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(124, 16, 52));
        jLabel17.setText("Date  :");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 140, 60, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(124, 16, 52));
        jLabel18.setText("Serial No");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 60, -1));

        txt_totalInWords.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_totalInWords.setForeground(new java.awt.Color(124, 16, 52));
        txt_totalInWords.setText("total in word");
        jPanel1.add(txt_totalInWords, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 440, 450, 20));

        txt_courseName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_courseName.setForeground(new java.awt.Color(124, 16, 52));
        txt_courseName.setText("1");
        jPanel1.add(txt_courseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 240, -1));

        jSeparator4.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator4.setForeground(new java.awt.Color(124, 16, 52));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 890, 20));

        txt_amount1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_amount1.setForeground(new java.awt.Color(124, 16, 52));
        txt_amount1.setText("1");
        jPanel1.add(txt_amount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 340, 100, -1));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(124, 16, 52));
        jLabel20.setText("1");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 60, -1));

        txt_remark.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_remark.setForeground(new java.awt.Color(124, 16, 52));
        txt_remark.setText("Remark");
        jPanel1.add(txt_remark, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 530, 460, -1));

        jSeparator5.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator5.setForeground(new java.awt.Color(124, 16, 52));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 400, 170, 10));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(124, 16, 52));
        jLabel23.setText("Total in words  :");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 110, -1));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(124, 16, 52));
        jLabel24.setText("Remark          :");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 110, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(124, 16, 52));
        jLabel19.setText("Head");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, 60, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(124, 16, 52));
        jLabel22.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jLabel22ComponentShown(evt);
            }
        });
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 540, 130, 20));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(124, 16, 52));
        jLabel25.setText("Receivers Signature");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 520, 130, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 890, 570));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(124, 16, 52));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/printer-.png"))); // NOI18N
        jLabel1.setText(" Print Receipt");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 4, 210, 40));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 230, 50));

        setSize(new java.awt.Dimension(1284, 687));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelHome.setBackground(clr);

    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelHome.setBackground(clr);

    }//GEN-LAST:event_btnHomeMouseExited

    private void btnSearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelSearch.setBackground(clr);
    }//GEN-LAST:event_btnSearchMouseEntered

    private void btnSearchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelSearch.setBackground(clr);
    }//GEN-LAST:event_btnSearchMouseExited

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelEdit.setBackground(clr);
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelEdit.setBackground(clr);
    }//GEN-LAST:event_btnEditMouseExited

    private void btnCourseListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCourseListMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelCourseList.setBackground(clr);
    }//GEN-LAST:event_btnCourseListMouseEntered

    private void btnCourseListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCourseListMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelCourseList.setBackground(clr);
    }//GEN-LAST:event_btnCourseListMouseExited

    private void btnViewAllRecordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllRecordMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelViewAllRecord.setBackground(clr);
    }//GEN-LAST:event_btnViewAllRecordMouseEntered

    private void btnViewAllRecordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllRecordMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelViewAllRecord.setBackground(clr);
    }//GEN-LAST:event_btnViewAllRecordMouseExited

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelLogout.setBackground(clr);
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelLogout.setBackground(clr);
    }//GEN-LAST:event_btnLogoutMouseExited

    private void btn_printMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_printMouseEntered

    private void btn_printMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_printMouseExited

    private void btn_editMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editMouseEntered

    private void btn_editMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editMouseExited

    private void btnViewAllRecord1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllRecord1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewAllRecord1MouseEntered

    private void btnViewAllRecord1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllRecord1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewAllRecord1MouseExited

    private void btn_printMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseClicked
       
        PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Print Data");
            
            job.setPrintable(new Printable(){
            public int print(Graphics pg,PageFormat pf, int pageNum){
                    pf.setOrientation(PageFormat.LANDSCAPE);
                 if(pageNum > 0){
                    return Printable.NO_SUCH_PAGE;
                }
                
                Graphics2D g2 = (Graphics2D)pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(0.47,0.47);
                
                jPanel1.print(g2);
         
               
                return Printable.PAGE_EXISTS;
                         
                
            }
    });
            boolean ok = job.printDialog();
        if(ok){
        try{
            
        job.print();
        }
        catch (PrinterException ex){
	ex.printStackTrace();
}
        }
    }//GEN-LAST:event_btn_printMouseClicked

    private void btn_editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editMouseClicked
        // TODO add your handling code here:
        
        UpdateFeesDetails update=new UpdateFeesDetails();
        update.setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btn_editMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
  home home2=new home();
        home2.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
SearchRecordWithEditRecord search= new SearchRecordWithEditRecord();
        search.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
EditCourse eC =new EditCourse();
        eC.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnCourseListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCourseListMouseClicked
        // TODO add your handling code here
         ViewCourse vC = new ViewCourse();
        vC.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCourseListMouseClicked

    private void btnViewAllRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllRecordMouseClicked
  ViewAllRecords vAR =new ViewAllRecords();
        vAR.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewAllRecordMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
 AccountantLogin lg =new AccountantLogin();
        lg.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void jLabel22ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel22ComponentShown
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jLabel22ComponentShown

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        EditStudent es= new EditStudent();
        es.show();
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelEditStudent.setBackground(clr);
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelEditStudent.setBackground(clr);
    }//GEN-LAST:event_jLabel2MouseExited

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
            java.util.logging.Logger.getLogger(PrintReciept1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrintReciept1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrintReciept1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrintReciept1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrintReciept1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnCourseList;
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnHome;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnViewAllRecord;
    private javax.swing.JLabel btnViewAllRecord1;
    private javax.swing.JLabel btn_edit;
    private javax.swing.JLabel btn_print;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPanel panelBack1;
    private javax.swing.JPanel panelBack2;
    private javax.swing.JPanel panelCourseList;
    private javax.swing.JPanel panelEdit;
    private javax.swing.JPanel panelEditStudent;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelLogout;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelViewAllRecord;
    private javax.swing.JPanel panelViewAllRecord1;
    private javax.swing.JPanel panelsideBar;
    private javax.swing.JLabel txt_amount1;
    private javax.swing.JLabel txt_bankName;
    private javax.swing.JLabel txt_cheque_esewa;
    private javax.swing.JLabel txt_courseName;
    private javax.swing.JLabel txt_date;
    private javax.swing.JLabel txt_paymentMode;
    private javax.swing.JLabel txt_receiptNo;
    private javax.swing.JLabel txt_receivedFrom;
    private javax.swing.JLabel txt_remark;
    private javax.swing.JLabel txt_total;
    private javax.swing.JLabel txt_totalInWords;
    // End of variables declaration//GEN-END:variables
}
