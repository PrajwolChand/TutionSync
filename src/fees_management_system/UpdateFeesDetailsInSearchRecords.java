/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fees_management_system;

import java.awt.Color;
import javax.swing.*; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
/**
 *
 * @author prajwol
 */
public class UpdateFeesDetailsInSearchRecords extends javax.swing.JFrame {

    /**
     * Creates new form AddFees
     */
    String receipt_no;
    
    public UpdateFeesDetailsInSearchRecords() {
      
    }

    public UpdateFeesDetailsInSearchRecords(String receiptnumber){
        
        this.receipt_no=receiptnumber;
        
      initComponents();
        displayCashFirst();
        fillComboBox();
        
        //setupUI();
        
        
        txt_receiptNo.setText(receipt_no);
        System.out.println(receipt_no);
        
      // int recieptNo=getReceiptNo();
      // txt_receiptNo.setText(Integer.toString(recieptNo ));
       
       setRecords();
    }
    
    public  void displayCashFirst(){
            lbl_DDNo.setVisible(false);
            lbl_ChequeNo.setVisible(false);
            lbl_bankName.setVisible(false);
            
            txt_ChequeNo.setVisible(false);
            txt_DDNo.setVisible(false);
            txt_bankName.setVisible(false);
    }
    
    public boolean validation(){
            

if (txt_receivedFrom.getText().equals("")||txt_receivedFrom.getText().matches("[a-zA-Z]+")==false) { 
    JOptionPane.showMessageDialog(this, "please enter Valid user name");
    return false;
} 


if (jDateChooser1.getDate() == null) {
    JOptionPane.showMessageDialog(this, "please select a date");
    return false;
}



if (txt_amount.getText().equals("") || txt_amount.getText().matches("[0-9]+")== false ) {
        JOptionPane.showMessageDialog(this, "please enter amount (in numbers) "); 

        return false;

}

  if(combo_PaymentMode.getSelectedItem().toString().equalsIgnoreCase("cheque")){

   if (txt_ChequeNo.getText().equals("")|| txt_ChequeNo.getText().matches("[0-9]+")== false) {

    JOptionPane.showMessageDialog(this, "please enter Valid cheque number"); 
    return false;
}

 if (txt_bankName.getText().equals("")||txt_bankName.getText().matches("[a-zA-Z]+") == false) {

  JOptionPane.showMessageDialog(this, "please enter bank name");

  return false;

  }
 
}
        
   if (combo_PaymentMode.getSelectedItem().toString().equalsIgnoreCase ("E-Sewa")) {
       
       if (txt_DDNo.getText().equals("")|| txt_DDNo.getText().matches("[0-9]+")== false) {

   JOptionPane.showMessageDialog(this, "please enter Valid E-Sewa no"); 
    return false;

        }
       
  if (txt_transactioncode.getText().equals("")) {

   JOptionPane.showMessageDialog(this, "please enter Transaction code"); 
    return false;

        }

   }

   if (combo_PaymentMode.getSelectedItem().toString().equalsIgnoreCase ("card")) {

   if (txt_bankName.getText().equals("")||txt_bankName.getText().matches("[a-zA-Z]+") == false) {

    JOptionPane.showMessageDialog(this, "please enter  Valid bank name");

    return false;

   }

   }
   if (txt_Rollno.getText().equals("")|| txt_Rollno.getText().matches("[0-9]+")== false){
    JOptionPane.showMessageDialog(this, "please enter Roll no"); 
    return false;
   }

        
        return true;
    }
    
    public void fillComboBox () {

     try{

            Class.forName ("org.apache.derby.jdbc.EmbeddedDriver");

            Connection con = DriverManager.getConnection ("jdbc:derby://localhost:1527/fees_management","root","root"); 
            PreparedStatement pst = con.prepareStatement ("select cname from course");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

            comboCourse.addItem(rs.getString("cname"));
} 

}catch (Exception e) { e.printStackTrace();


    }
 }
    
   
    /*public int getReceiptNo () {

        int receiptNo = 0;

        try {

        Connection con= DBConnection.getConnection(); 
        PreparedStatement pst = con.prepareStatement ("select max (reciept_no) from FEES_DETAILS_DD"); 
        ResultSet rs = pst.executeQuery();

        if (rs.next() == true) {

    receiptNo = rs.getInt(1);

    }

        } catch (Exception e) {

    e.printStackTrace();

    }

    return receiptNo;

    }
  */
    
    public String updateData() {

        String status="";
        
int recieptNo = Integer.parseInt(txt_receiptNo.getText());

String studentName = txt_receivedFrom.getText();

String rollNo = (txt_Rollno.getText());

String paymentMode = combo_PaymentMode.getSelectedItem().toString();

String chequeNo= txt_ChequeNo.getText();

String bankName = txt_bankName.getText();

String transCode= txt_transactioncode.getText();

String esewa_no = txt_DDNo.getText();

String courseName = txt_CourseName.getText();



float totalAmount = Float.parseFloat (txt_total_number.getText());

SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

String date = dateFormat.format(jDateChooser1.getDate());

float initialAmount = Float.parseFloat (txt_amount.getText());


String totalInWords = txt_total_words.getText();

String remark= txt_remarks.getText();


    try {
        


Connection con= DBConnection.getConnection();

PreparedStatement pst = con.prepareStatement ("update fees_details_dd set student_name = ?, roll_no = ?,"

+" payment_mode = ?, cheque_no = ?, bank_name = ?, esewa_no = ?, transaction_code = ?, course_name = ?, total_amount = ?, date = ? ,"

+" amount = ?, total_in_words = ?, remark = ? where reciept_no = ?");



pst.setString (1, studentName);

pst.setString (2, rollNo);

pst.setString (3, paymentMode);

pst.setString (4, chequeNo);

pst.setString (5, bankName);

pst.setString (6, esewa_no);

pst.setString(7, transCode);

pst.setString (8, courseName);



pst.setFloat (9, totalAmount);

pst.setString(10, date);

pst.setFloat (11, initialAmount);

pst.setString (12, totalInWords);

pst.setString (13, remark);

pst.setInt(14, recieptNo);



  int rowCount=pst.executeUpdate();
  
  if(rowCount == 1){
    status="Success";  
  }else{
      status="Failed";
  }
    }catch(Exception e){
        e.printStackTrace();
    }
    return status;
    }
    
    
    public void setRecords(){
       

try {

Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

Connection con =DriverManager.getConnection ("jdbc:derby://localhost:1527/fees_management","root","root");

PreparedStatement pst = con.prepareStatement ("select * from fees_details_dd where reciept_no = ?");

pst.setInt(1, Integer.parseInt(receipt_no ));
ResultSet rs = pst.executeQuery(); 


rs.next();


txt_receiptNo.setText(rs.getString("reciept_no"));

combo_PaymentMode.setSelectedItem (rs.getString("payment_mode"));

txt_ChequeNo.setText(rs.getString("cheque_no"));

txt_DDNo.setText(rs.getString("esewa_no")); 

txt_transactioncode.setText(rs.getString("transaction_code"));

txt_bankName.setText(rs.getString("bank_name"));

txt_receivedFrom.setText (rs.getString("student_name"));

jDateChooser1.setDate(rs.getDate("date"));





txt_Rollno.setText(rs.getString("roll_no"));

comboCourse.setSelectedItem(rs.getString("course_name"));

txt_amount.setText(rs.getString("amount"));


txt_total_number.setText(rs.getString("total_amount"));

txt_total_words.setText (rs.getString("total_in_words"));

txt_remarks.setText (rs.getString("remark"));

}catch(Exception e){

e.printStackTrace();

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
        panelParent = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbl_DDNo = new javax.swing.JLabel();
        lbl_ChequeNo = new javax.swing.JLabel();
        lbl_bankName = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_bankName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_receiptNo = new javax.swing.JTextField();
        txt_ChequeNo = new javax.swing.JTextField();
        combo_PaymentMode = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        panelChild = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_Rollno = new javax.swing.JTextField();
        comboCourse = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_vat = new javax.swing.JTextField();
        txt_receivedFrom = new javax.swing.JTextField();
        txt_tax = new javax.swing.JTextField();
        txt_total_number = new javax.swing.JTextField();
        txt_amount = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        txt_total_words = new javax.swing.JTextField();
        txt_CourseName = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_remarks = new javax.swing.JTextArea();
        btn_print = new javax.swing.JButton();
        lbl_receivedFrom = new javax.swing.JLabel();
        txt_DDNo = new javax.swing.JTextField();
        txt_transactioncode = new javax.swing.JTextField();
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
        panelBack = new javax.swing.JPanel();
        btnBack = new javax.swing.JLabel();
        panelEditStudent = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelLogout = new javax.swing.JPanel();
        btnLogout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelsideBar.setBackground(new java.awt.Color(124, 16, 52));
        panelsideBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelParent.setBackground(new java.awt.Color(250, 171, 153));
        panelParent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel2.setText("Receipt no  : ");
        panelParent.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 120, 20));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel3.setText("Mode of Payment :");
        panelParent.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 150, 20));

        lbl_DDNo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        lbl_DDNo.setText("E-Sewa No     :");
        panelParent.add(lbl_DDNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 150, 20));

        lbl_ChequeNo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        lbl_ChequeNo.setText("Cheque no  :");
        panelParent.add(lbl_ChequeNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 120, 20));

        lbl_bankName.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        lbl_bankName.setText("Bank name  :");
        panelParent.add(lbl_bankName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 160, 20));

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel9.setText("SWS -");
        panelParent.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 60, 20));
        panelParent.add(txt_bankName, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 130, -1));

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel10.setText("Date  :");
        panelParent.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, 120, 20));
        panelParent.add(txt_receiptNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 130, -1));
        panelParent.add(txt_ChequeNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 130, -1));

        combo_PaymentMode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "E-Sewa", "Cheque", "Cash", "Card" }));
        combo_PaymentMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_PaymentModeActionPerformed(evt);
            }
        });
        panelParent.add(combo_PaymentMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 130, -1));
        panelParent.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, 130, -1));

        panelChild.setBackground(new java.awt.Color(250, 171, 153));
        panelChild.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel8.setText("Amount(Rs)");
        panelChild.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 120, 20));

        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel12.setText("Roll no:");
        panelChild.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 60, 20));
        panelChild.add(txt_Rollno, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 90, 110, -1));

        comboCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCourseActionPerformed(evt);
            }
        });
        panelChild.add(comboCourse, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 130, -1));

        jSeparator1.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator1.setForeground(new java.awt.Color(124, 16, 52));
        panelChild.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 760, 20));

        jSeparator2.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator2.setForeground(new java.awt.Color(124, 16, 52));
        panelChild.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 172, 760, 20));

        jLabel15.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel15.setText("Receiver's Signature");
        panelChild.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 410, 170, 20));

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel16.setText("Sr. no.");
        panelChild.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 120, 20));

        jLabel17.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel17.setText("Heads");
        panelChild.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 120, 20));
        panelChild.add(txt_vat, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, 160, -1));
        panelChild.add(txt_receivedFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 130, -1));
        panelChild.add(txt_tax, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 280, 160, -1));
        panelChild.add(txt_total_number, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 320, 160, -1));

        txt_amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_amountActionPerformed(evt);
            }
        });
        panelChild.add(txt_amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 220, 160, -1));

        jSeparator3.setBackground(new java.awt.Color(124, 16, 52));
        jSeparator3.setForeground(new java.awt.Color(124, 16, 52));
        panelChild.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 400, 210, 10));

        jLabel18.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel18.setText("Course  :");
        panelChild.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 120, 20));
        panelChild.add(txt_total_words, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 340, -1));
        panelChild.add(txt_CourseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 340, -1));

        jSeparator4.setForeground(new java.awt.Color(124, 16, 52));
        panelChild.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, 210, 10));

        jLabel19.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel19.setText("Remarks  :");
        panelChild.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 150, 20));

        jLabel20.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jLabel20.setText("Totall In words  :");
        panelChild.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 130, 20));

        txt_remarks.setColumns(20);
        txt_remarks.setRows(5);
        jScrollPane1.setViewportView(txt_remarks);

        panelChild.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 370, 340, 60));

        btn_print.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btn_print.setText("Print");
        btn_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printActionPerformed(evt);
            }
        });
        panelChild.add(btn_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 440, 90, 30));

        lbl_receivedFrom.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        lbl_receivedFrom.setText("Received From  :");
        panelChild.add(lbl_receivedFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 140, 20));

        panelParent.add(panelChild, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 1030, 480));
        panelParent.add(txt_DDNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 130, -1));
        panelParent.add(txt_transactioncode, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 130, -1));

        panelsideBar.add(panelParent, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 910, 800));

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
        panelHome.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 200, 50));

        panelsideBar.add(panelHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 270, 70));

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
        panelSearch.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 230, -1));

        panelsideBar.add(panelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 270, 70));

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
        panelEdit.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 200, 60));

        panelsideBar.add(panelEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 270, 70));

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

        panelsideBar.add(panelCourseList, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 270, 70));

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
        panelViewAllRecord.add(btnViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 230, 50));

        panelsideBar.add(panelViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 270, 70));

        panelBack.setBackground(new java.awt.Color(124, 16, 52));
        panelBack.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelBack.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/ADDITION.png"))); // NOI18N
        btnBack.setText("    Add Fees");
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBackMouseExited(evt);
            }
        });
        panelBack.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 200, 50));

        panelsideBar.add(panelBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 270, 70));

        panelEditStudent.setBackground(new java.awt.Color(124, 16, 52));
        panelEditStudent.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), java.awt.Color.white, null, null));
        panelEditStudent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/student.png"))); // NOI18N
        jLabel1.setText("   Edit Student");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });
        panelEditStudent.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, 40));

        panelsideBar.add(panelEditStudent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 270, 70));

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
        panelLogout.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 200, 50));

        panelsideBar.add(panelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 570, 270, 70));

        getContentPane().add(panelsideBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        setSize(new java.awt.Dimension(1284, 687));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printActionPerformed
        // TODO add your handling code here:
        
        if (validation()==true){
            String result=updateData();
            
            if(result.equals("Success")){
                JOptionPane.showMessageDialog(this,"Record updated Successfully");
                PrintReciept p= new PrintReciept();
                p.setVisible(true);
                this.dispose();
                
            }else{
                JOptionPane.showMessageDialog(this,"Record updation failed");
            }
            
        }
          
    }//GEN-LAST:event_btn_printActionPerformed

    
    private void combo_PaymentModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_PaymentModeActionPerformed
        // TODO add your handling code here:
            
            
         if( combo_PaymentMode.getSelectedIndex()==0){
            lbl_DDNo.setVisible(true);
            txt_DDNo.setVisible(true);
            lbl_ChequeNo.setVisible(false);
            txt_ChequeNo.setVisible(false);
            lbl_bankName.setText("Transaction Code  :");
            lbl_bankName.setVisible(true);
            txt_transactioncode.setVisible(true);
            txt_bankName.setVisible(false);
        }
         if( combo_PaymentMode.getSelectedIndex()==1){
            lbl_DDNo.setVisible(false);
            txt_DDNo.setVisible(false);
            lbl_ChequeNo.setVisible(true);
            txt_ChequeNo.setVisible(true);
            lbl_bankName.setText("Bank Name  :");
            lbl_bankName.setVisible(true); 
            txt_bankName.setVisible(true);
            txt_transactioncode.setVisible(false);
        }
         if( combo_PaymentMode.getSelectedIndex()==2){
            lbl_DDNo.setVisible(false);
            txt_DDNo.setVisible(false);
            lbl_ChequeNo.setVisible(false);
            txt_ChequeNo.setVisible(false);
            lbl_bankName.setVisible(false); 
            txt_bankName.setVisible(false);
            txt_transactioncode.setVisible(false);
        }
         if( combo_PaymentMode.getSelectedIndex()==3){
            lbl_DDNo.setVisible(false);
            txt_DDNo.setVisible(false);
            lbl_ChequeNo.setVisible(false);
            txt_ChequeNo.setVisible(false);
            lbl_bankName.setText("Bank Name  :");
            lbl_bankName.setVisible(true); 
            txt_bankName.setVisible(true);
            txt_transactioncode.setVisible(false);
        }
            
    }//GEN-LAST:event_combo_PaymentModeActionPerformed

    private void txt_amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_amountActionPerformed
        // TODO add your handling code here:
        
        float amt=Float.parseFloat(txt_amount.getText());
        txt_total_number.setText((Float.toString(amt)));
        txt_total_words.setText(NumberToWordsConverter.convert((int)amt)+" only");
        
    }//GEN-LAST:event_txt_amountActionPerformed

    
    
    private void comboCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCourseActionPerformed
        // TODO add your handling code here:
        
        txt_CourseName.setText(comboCourse.getSelectedItem().toString());
    }//GEN-LAST:event_comboCourseActionPerformed

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        home home2=new home();
        home2.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeMouseClicked

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

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        // TODO add your handling code here:
        SearchRecordWithEditRecord search= new SearchRecordWithEditRecord();
        search.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSearchMouseClicked

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

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        // TODO add your handling code here:
        EditCourse eC =new EditCourse();
        eC.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnEditMouseClicked

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

    private void btnCourseListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCourseListMouseClicked
        // TODO add your handling code here:
        ViewCourse vC = new ViewCourse();
        vC.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCourseListMouseClicked

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

    private void btnViewAllRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllRecordMouseClicked
        // TODO add your handling code here:
        ViewAllRecords vAR =new ViewAllRecords();
        vAR.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnViewAllRecordMouseClicked

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

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
        AddFees ad= new AddFees();
        ad.show();
        this.dispose();
    }//GEN-LAST:event_btnBackMouseClicked

    private void btnBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelBack.setBackground(clr);
    }//GEN-LAST:event_btnBackMouseEntered

    private void btnBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelBack.setBackground(clr);
    }//GEN-LAST:event_btnBackMouseExited

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        EditStudent es= new EditStudent();
        es.show();
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelEditStudent.setBackground(clr);
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelEditStudent.setBackground(clr);
    }//GEN-LAST:event_jLabel1MouseExited

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        Login1 lg =new Login1();
        lg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutMouseClicked

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

    
    
    
    
    
   // Existing code in your class
//
  //to move the focus to another filed on pressing tab
    /*
public void setupUI() {
    // Existing code to set up your UI components

    // Add the key event dispatcher code within your method
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof JTextField) {
                    JTextField currentTextField = (JTextField) KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (currentTextField == txt_receivedFrom) {
                        txt_receivedFrom.requestFocus();
                    } else if (currentTextField == txt_Rollno) {
                        txt_amount.requestFocus();
                    }
                }
            }
            return false;
        }
    });

    // More existing code in your method
}

// More existing code in your class
*/
    
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
            java.util.logging.Logger.getLogger(UpdateFeesDetailsInSearchRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateFeesDetailsInSearchRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateFeesDetailsInSearchRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateFeesDetailsInSearchRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateFeesDetailsInSearchRecords().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnBack;
    private javax.swing.JLabel btnCourseList;
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnHome;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnViewAllRecord;
    private javax.swing.JButton btn_print;
    private javax.swing.JComboBox<String> comboCourse;
    private javax.swing.JComboBox<String> combo_PaymentMode;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lbl_ChequeNo;
    private javax.swing.JLabel lbl_DDNo;
    private javax.swing.JLabel lbl_bankName;
    private javax.swing.JLabel lbl_receivedFrom;
    private javax.swing.JPanel panelBack;
    private javax.swing.JPanel panelChild;
    private javax.swing.JPanel panelCourseList;
    private javax.swing.JPanel panelEdit;
    private javax.swing.JPanel panelEditStudent;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelLogout;
    private javax.swing.JPanel panelParent;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelViewAllRecord;
    private javax.swing.JPanel panelsideBar;
    private javax.swing.JTextField txt_ChequeNo;
    private javax.swing.JTextField txt_CourseName;
    private javax.swing.JTextField txt_DDNo;
    private javax.swing.JTextField txt_Rollno;
    private javax.swing.JTextField txt_amount;
    private javax.swing.JTextField txt_bankName;
    private javax.swing.JTextField txt_receiptNo;
    private javax.swing.JTextField txt_receivedFrom;
    private javax.swing.JTextArea txt_remarks;
    private javax.swing.JTextField txt_tax;
    private javax.swing.JTextField txt_total_number;
    private javax.swing.JTextField txt_total_words;
    private javax.swing.JTextField txt_transactioncode;
    private javax.swing.JTextField txt_vat;
    // End of variables declaration//GEN-END:variables
}
