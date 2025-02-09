/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fees_management_system;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author prajwol
 */
public class EditStudentManager extends javax.swing.JFrame {

    /**
     * Creates new form EditCourse
     */
    DefaultTableModel model;
    public EditStudentManager() {
        initComponents();
        setRecordsToTable();
    }
    
    
  public void setRecordsToTable() {
    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement("SELECT * FROM student_details ORDER BY id ASC");
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String studentId = rs.getString("id");
            String sname = rs.getString("student_name");
            String rollno = rs.getString("roll_no");
            String password = rs.getString("password");

            // Optionally decrypt the password for display, if required
            // int key = 7; // Key used for encryption
            // String decryptedPassword = customDecryptPassword(password, key);

            Object[] obj = {studentId, sname, rollno, password}; // Use decryptedPassword if you choose to decrypt

            model = (DefaultTableModel) tbl_studentData.getModel();
            model.addRow(obj);
            tbl_studentData.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void clearTable() {
    DefaultTableModel model = (DefaultTableModel) tbl_studentData.getModel();
    model.setRowCount(0);
}

public void addStudent(int id, String sname, String rollno, String password) {
    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement("insert into student_details values(?,?,?,?)");

        // Encrypt the password using the custom algorithm
        int key = 7; // Secret key for XOR
        String encryptedPassword = customEncryptPassword(password, key);

        pst.setInt(1, id);
        pst.setString(2, sname);
        pst.setString(3, rollno);
        pst.setString(4, encryptedPassword); // Use the encrypted password

        int rowCount = pst.executeUpdate();

        if (rowCount == 1) {
            JOptionPane.showMessageDialog(this, "Student added successfully");
            clearTable();
            setRecordsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Student insertion failed");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Student insertion failed");
        e.printStackTrace();
    }
}

public void update(int id, String sname, String rollno, String password) {
    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement("update student_details set student_name = ?, roll_no = ?, password = ? where id = ?");

        // Encrypt the password using the custom algorithm
        int key = 7; // Secret key for XOR
        String encryptedPassword = customEncryptPassword(password, key);

        pst.setString(1, sname);
        pst.setString(2, rollno);
        pst.setString(3, encryptedPassword); // Use the encrypted password
        pst.setInt(4, id);

        int rowCount = pst.executeUpdate();

        if (rowCount == 1) {
            JOptionPane.showMessageDialog(this, "Student updated successfully");
            clearTable();
            setRecordsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Student update failed");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Student update failed");
        e.printStackTrace();
    }
}

public void delete(int id) {
    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement("delete from student_details where id = ?");
        
        pst.setInt(1, id);

        int rowCount = pst.executeUpdate();

        if (rowCount == 1) {
            JOptionPane.showMessageDialog(this, "Student deleted successfully");
            clearTable();
            setRecordsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Student deletion failed");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Student deletion failed");
        e.printStackTrace();
    }
}

// Custom encryption method (already defined in your code)
public String customEncryptPassword(String password, int key) {
    StringBuilder encryptedPassword = new StringBuilder();
    
    int shift = 3; // Substitution shift value (can be customized)
    for (char c : password.toCharArray()) {
        // Substitution: shift each character
        char substitutedChar = (char) (c + shift);
        
        // XOR with the key
        char encryptedChar = (char) (substitutedChar ^ key);
        
        encryptedPassword.append(encryptedChar);
    }
    
    return encryptedPassword.toString();
}

     
     
     //this gives the detailed student info about courses and remaining fees in ecah courses
private void fetchStudentDataAndUpdateLabel(String rollNo) {
    try {
        Connection con = DBConnection.getConnection();

        // Query to get the student's name, course fee, and student payments
        PreparedStatement pst = con.prepareStatement(
            "SELECT fd.student_name, fd.course_name, SUM(fd.total_amount) AS total_paid, c.cost AS course_fee " +
            "FROM fees_details_dd fd " +
            "JOIN course c ON fd.course_name = c.cname " +
            "WHERE fd.roll_no = ? " +
            "GROUP BY fd.student_name, fd.course_name, c.cost"
        );
        pst.setString(1, rollNo);
        ResultSet rs = pst.executeQuery();

        // Variables to store student name and payment details
        String studentName = "";
        StringBuilder coursesEnrolled = new StringBuilder();
        StringBuilder details = new StringBuilder();  // For displaying course details
        float amtPaid = 0;
        float amtToPay = 0;

        while (rs.next()) {
            // Get the student name once
            if (studentName.isEmpty()) {
                studentName = rs.getString("student_name");
            }

            String courseName = rs.getString("course_name");
            float paidForCourse = rs.getFloat("total_paid");
            float totalCourseFee = rs.getFloat("course_fee");

            // Add course to the list of enrolled courses
            coursesEnrolled.append(courseName).append(", ");

            // Handle overpayments (re-enrollments)
            if (paidForCourse > totalCourseFee) {
                float remainingPaidAmount = paidForCourse;

                while (remainingPaidAmount > 0) {
                    float toPayForThisEnrollment = Math.min(totalCourseFee, remainingPaidAmount);
                    remainingPaidAmount -= toPayForThisEnrollment;

                    amtPaid += toPayForThisEnrollment;
                    amtToPay += Math.max(0, totalCourseFee - toPayForThisEnrollment);

                    if (remainingPaidAmount > 0) {
                        coursesEnrolled.append(courseName).append(" (Re-enrolled), ");
                    }

                    // Append this enrollment's details
                    details.append("<b>Course:</b> ").append(courseName)
                           .append(", <b>Total Fee:</b> ₹").append(totalCourseFee)
                           .append(", <b>Paid:</b> ₹").append(toPayForThisEnrollment)
                           .append(", <b>Remaining:</b> ₹").append(Math.max(0, totalCourseFee - toPayForThisEnrollment))
                           .append("<br>");
                }
            } else {
                amtPaid += paidForCourse;
                amtToPay += Math.max(0, totalCourseFee - paidForCourse);

                // Append this enrollment's details
                details.append("<b>Course:</b> ").append(courseName)
                       .append(", <b>Total Fee:</b> ₹").append(totalCourseFee)
                       .append(", <b>Paid:</b> ₹").append(paidForCourse)
                       .append(", <b>Remaining:</b> ₹").append(Math.max(0, totalCourseFee - paidForCourse))
                       .append("<br>");
            }
        }

        // Remove trailing comma from coursesEnrolled
        if (coursesEnrolled.length() > 0) {
            coursesEnrolled.setLength(coursesEnrolled.length() - 2);  // Remove the last comma and space
        }

        // Build the final details string
        String finalDetails = "<html><b>Student Name: </b>" + studentName + "<br>" + 
                              "<b>Courses Enrolled:</b> " + coursesEnrolled.toString() + "<br>" + 
                              details.toString() + 
                              "<b>Total Paid: </b>₹" + amtPaid + "<br>" +
                              "<b>Total Remaining: </b>₹" + amtToPay + "</html>";

        // Update the current frame's lbl_details
        //lbl_details.setText(finalDetails);
       

        // Create a new StickyInfoStudent frame and pass the details to it
        StickyInfoStudent is = new StickyInfoStudent(finalDetails);
        is.show();
        
        
    } catch (Exception e) {
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_studentData = new javax.swing.JTable();
        txt_studentName = new javax.swing.JTextField();
        txt_rollno = new javax.swing.JTextField();
        txt_studentId = new javax.swing.JTextField();
        lbl_courseName = new javax.swing.JLabel();
        lbl_coursePrice = new javax.swing.JLabel();
        lbl_courseId = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_coursePrice1 = new javax.swing.JLabel();
        txt_password = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
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
        panelBack = new javax.swing.JPanel();
        btnBack = new javax.swing.JLabel();
        panelLogout = new javax.swing.JPanel();
        btnLogout = new javax.swing.JLabel();
        panelEditStudent = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelEditAccountant = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(250, 171, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_studentData.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbl_studentData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student Id", "Student Name", "Roll No. ", "Password"
            }
        ));
        tbl_studentData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_studentDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_studentData);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 550, 450));

        txt_studentName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel1.add(txt_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 250, 170, 40));

        txt_rollno.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel1.add(txt_rollno, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 310, 170, 40));

        txt_studentId.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_studentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_studentIdActionPerformed(evt);
            }
        });
        jPanel1.add(txt_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 180, 170, 40));

        lbl_courseName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_courseName.setForeground(new java.awt.Color(255, 255, 255));
        lbl_courseName.setText("Student  Name :");
        jPanel1.add(lbl_courseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, 160, -1));

        lbl_coursePrice.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_coursePrice.setForeground(new java.awt.Color(255, 255, 255));
        lbl_coursePrice.setText("Roll No  :");
        jPanel1.add(lbl_coursePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 320, 140, -1));

        lbl_courseId.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_courseId.setForeground(new java.awt.Color(255, 255, 255));
        lbl_courseId.setText("Student Id       :");
        jPanel1.add(lbl_courseId, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, 140, -1));

        jButton1.setBackground(new java.awt.Color(124, 16, 52));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 460, 100, 50));

        jButton2.setBackground(new java.awt.Color(124, 16, 52));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 460, 100, 50));

        jButton3.setBackground(new java.awt.Color(124, 16, 52));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Add");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 100, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/back1.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 30, 40, 30));

        jLabel6.setBackground(new java.awt.Color(124, 16, 52));
        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(124, 16, 52));
        jLabel6.setText("Edit Student");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, -1, -1));

        lbl_coursePrice1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_coursePrice1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_coursePrice1.setText("Password :");
        jPanel1.add(lbl_coursePrice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 390, 140, -1));

        txt_password.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel1.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 380, 170, 40));

        jButton4.setBackground(new java.awt.Color(124, 16, 52));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("View Student Info");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 530, 310, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 980, 650));

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
        panelHome.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 200, 50));

        panelsideBar.add(panelHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 270, 70));

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

        panelsideBar.add(panelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 270, 70));

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

        panelsideBar.add(panelEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 270, 70));

        panelCourseList.setBackground(new java.awt.Color(124, 16, 52));
        panelCourseList.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelCourseList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCourseList.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnCourseList.setForeground(new java.awt.Color(255, 255, 255));
        btnCourseList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/list_1.png"))); // NOI18N
        btnCourseList.setText("View Report");
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

        panelsideBar.add(panelCourseList, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 270, 70));

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

        panelsideBar.add(panelViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 270, 70));

        panelBack.setBackground(new java.awt.Color(124, 16, 52));
        panelBack.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(255, 255, 255), null, null));
        panelBack.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/stats.png"))); // NOI18N
        btnBack.setText("     Predict Fees");
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

        panelsideBar.add(panelBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 270, 70));

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

        panelsideBar.add(panelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, 270, 70));

        panelEditStudent.setBackground(new java.awt.Color(124, 16, 52));
        panelEditStudent.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.white, null, null));
        panelEditStudent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Edit Students");
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
        panelEditStudent.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 30));

        panelsideBar.add(panelEditStudent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 120, 50));

        panelEditAccountant.setBackground(new java.awt.Color(124, 16, 52));
        panelEditAccountant.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.white, null, null));
        panelEditAccountant.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Edit Accountant");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        panelEditAccountant.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 30));

        panelsideBar.add(panelEditAccountant, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 500, 140, 50));

        getContentPane().add(panelsideBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 310, 650));

        setSize(new java.awt.Dimension(1284, 687));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(txt_studentId.getText());
     
        
        delete(id);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(txt_studentId.getText());
        String sname=txt_studentName.getText();
        String rollno= txt_rollno.getText();
        String password=txt_password.getText();
        
        update(id, sname, rollno, password );
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
          int id = Integer.parseInt(txt_studentId.getText());
        String sname=txt_studentName.getText();
        String rollno= txt_rollno.getText();
        String password=txt_password.getText();
        
         addStudent(id, sname, rollno, password );
        
       
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tbl_studentDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_studentDataMouseClicked
        // TODO add your handling code here:
        
        int rowNo =tbl_studentData.getSelectedRow();
        TableModel model= tbl_studentData.getModel();
        txt_studentId.setText(model.getValueAt(rowNo, 0).toString());
        txt_studentName.setText((String)model.getValueAt(rowNo, 1));
        txt_rollno.setText(model.getValueAt(rowNo, 2).toString());
        txt_password.setText(model.getValueAt(rowNo, 3).toString());
    }//GEN-LAST:event_tbl_studentDataMouseClicked

    private void txt_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentIdActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        home hm=new home();
        hm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String rollno= txt_rollno.getText();
        fetchStudentDataAndUpdateLabel(rollno);
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        homeManager hm=new homeManager();
        hm.setVisible(true);
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
        SearchRecord search= new SearchRecord();
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
        EditCourseManager ecm= new EditCourseManager();
        ecm.show();
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
        GenerateReport vC = new GenerateReport();
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
        ViewAllRecordsManager vAR =new ViewAllRecordsManager();
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
        // TODO add your handling code here:
        PredictFees111 pp = new PredictFees111();
        pp.show();
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

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        ManagerLogin lg =new ManagerLogin();
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

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        EditStudentManager es=new EditStudentManager();
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

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        EditAccountantdummy eC =new EditAccountantdummy();
        eC.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        // TODO add your handling code here:
        Color clr= new Color(102,16,52);
        panelEditAccountant.setBackground(clr);
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        // TODO add your handling code here:
        Color clr= new Color(124,16,52);
        panelEditAccountant.setBackground(clr);
    }//GEN-LAST:event_jLabel3MouseExited

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
            java.util.logging.Logger.getLogger(EditStudentManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditStudentManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditStudentManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditStudentManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditStudentManager().setVisible(true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_courseId;
    private javax.swing.JLabel lbl_courseName;
    private javax.swing.JLabel lbl_coursePrice;
    private javax.swing.JLabel lbl_coursePrice1;
    private javax.swing.JPanel panelBack;
    private javax.swing.JPanel panelCourseList;
    private javax.swing.JPanel panelEdit;
    private javax.swing.JPanel panelEditAccountant;
    private javax.swing.JPanel panelEditStudent;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelLogout;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelViewAllRecord;
    private javax.swing.JPanel panelsideBar;
    private javax.swing.JTable tbl_studentData;
    private javax.swing.JTextField txt_password;
    private javax.swing.JTextField txt_rollno;
    private javax.swing.JTextField txt_studentId;
    private javax.swing.JTextField txt_studentName;
    // End of variables declaration//GEN-END:variables
}
