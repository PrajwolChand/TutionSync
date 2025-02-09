/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fees_management_system;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author prajwol
 */
public class StudentHomepage1 extends javax.swing.JFrame {

        private String rollNo;
    private String password;
    private String coursesEnrolled = "";
    private float amtPaid = 0;
    private float amtToPay = 0;
    
     // Course fees mapping
   /* private final Map<String, Float> courseFees = Map.of(
        "Java", 25000.0f,
        "C#", 10000.0f,
        "Python", 10000.0f,
        "Linux Training", 4000.0f,
        "Laravel", 15000.0f,
        "Basic IT Training", 2500.0f,
        "Wordpress", 15000.0f
    );*/
    /**
     * Creates new form StudentHomepage
     */
    public StudentHomepage1(String rollNo, String password) {
        this.rollNo = rollNo;  // store rollNo in a class variable
        this.password = password;  // store password in a class variable
        initComponents();
        displayStudentInfo(); // Display student details after initialization
        fetchStudentData();   // Fetch student records from the database
    }

    private StudentHomepage1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
private void fetchStudentData() {
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
        coursesEnrolled = "";
        amtPaid = 0;
        amtToPay = 0;

        while (rs.next()) {
            // Get the student name
            if (studentName.isEmpty()) {
                studentName = rs.getString("student_name");
            }

            String courseName = rs.getString("course_name");
            float paidForCourse = rs.getFloat("total_paid");
            float totalCourseFee = rs.getFloat("course_fee");

            // Add course to the list of enrolled courses
            coursesEnrolled += courseName + ", ";

            // Calculate how much has been paid and handle re-enrollment
            if (paidForCourse > totalCourseFee) {
                // If student has paid more than the course fee, consider it a re-enrollment
                // Calculate the payments across multiple enrollments
                float remainingPaidAmount = paidForCourse;
                while (remainingPaidAmount > 0) {
                    float toPayForThisEnrollment = Math.min(totalCourseFee, remainingPaidAmount);
                    remainingPaidAmount -= toPayForThisEnrollment;

                    // Update total amount paid
                    amtPaid += toPayForThisEnrollment;

                    // Calculate remaining amount to pay for the current enrollment
                    amtToPay += Math.max(0, totalCourseFee - toPayForThisEnrollment);

                    // If there is more remaining paid amount, it means they re-enrolled and fully paid
                    if (remainingPaidAmount > 0) {
                        // Add the re-enrollment course again
                        coursesEnrolled += courseName + " (Re-enrolled), ";
                    }
                }
            } else {
                // Normal scenario where student hasn't overpaid
                amtPaid += paidForCourse;
                amtToPay += Math.max(0, totalCourseFee - paidForCourse);
            }
        }

        // Remove trailing comma from the coursesEnrolled string if necessary
        if (!coursesEnrolled.isEmpty()) {
            coursesEnrolled = coursesEnrolled.substring(0, coursesEnrolled.length() - 2);
        }

        // Update the UI with fetched data
        updateUIWithStudentData(studentName);

    } catch (Exception e) {
        e.printStackTrace();
    }
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





 
 private void updateUIWithStudentData(String studentName) {
    // Display the roll number
    jLabel2.setText(rollNo);

    // Display the courses enrolled
    jLabel5.setText(coursesEnrolled);

    // Display total amount paid
    jLabel7.setText("₹ " + amtPaid);

    // Display remaining amount to pay
    jLabel1.setText("₹ " + amtToPay);

    // Update the welcome message with the student's name
    jLabel9.setText("Welcome " + studentName + "!");
}

    
private void displayStudentInfo() {
        // You can set the labels with the passed rollNo or other details
        jLabel2.setText(rollNo);  // Set the roll_no label (adjust this based on your component's name)
        // You can use the password if needed, but generally, password should not be displayed
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
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(124, 16, 52));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Student Homepage");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Learn about your dues here .");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 120));

        jPanel2.setBackground(new java.awt.Color(250, 171, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(124, 16, 52));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Your student Roll no.: ");
        jLabel3.setToolTipText("");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 37, -1, 40));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Courses inrolled in : ");
        jLabel4.setToolTipText("");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Fees paid till now :");
        jLabel8.setToolTipText("");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Payement Remaining :");
        jLabel10.setToolTipText("");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 180, 310));

        jPanel4.setBackground(new java.awt.Color(124, 16, 52));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("2001");
        jLabel2.setToolTipText("");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 480, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 500, 30));

        jPanel5.setBackground(new java.awt.Color(124, 16, 52));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Your courses");
        jLabel5.setToolTipText("");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 480, -1));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, 500, 30));

        jPanel6.setBackground(new java.awt.Color(124, 16, 52));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Fees paid till now ");
        jLabel7.setToolTipText("");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 480, -1));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 500, 30));

        jPanel7.setBackground(new java.awt.Color(124, 16, 52));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Payement Remaining ");
        jLabel1.setToolTipText("");
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 480, -1));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 500, 30));

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setText("Welcome Student Name !");
        jLabel9.setToolTipText("");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 490, -1));

        jButton1.setBackground(new java.awt.Color(124, 16, 52));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("View detailed Information");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 300, 180, 40));

        jButton2.setBackground(new java.awt.Color(124, 16, 52));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Log out");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 300, 180, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 740, 370));

        setSize(new java.awt.Dimension(752, 525));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        fetchStudentDataAndUpdateLabel(rollNo);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        StudentLogin lg =new StudentLogin();
        lg.show();
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(StudentHomepage1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentHomepage1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentHomepage1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentHomepage1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentHomepage1().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
