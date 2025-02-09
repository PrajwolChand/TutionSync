/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fees_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays; // Make sure to import Arrays
import java.util.Date;
import java.util.List;

/**
 *
 * @author prajwol
 */
public class PredictFees11 extends javax.swing.JFrame {

    /**
     * Creates new form PredictFees
     */
    public PredictFees11() {
        initComponents();
    }
    
public void predictFees() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // Get the future prediction range from the date pickers
    String fromDateStr = dateFormat.format(dateChooser_from.getDate());
    String toDateStr = dateFormat.format(dateChooser_to.getDate());

    List<Double> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();

    Date referenceDate = null;
    try {
        referenceDate = dateFormat.parse("2000-01-01");  // Reference date for normalization
    } catch (ParseException e) {
        e.printStackTrace();
    }

    // Collect past data for training the model
    try (Connection con = DBConnection.getConnection()) {
        PreparedStatement pst = con.prepareStatement("SELECT date, total_amount FROM fees_details_dd WHERE date < ? ORDER BY date");
        pst.setString(1, fromDateStr);  // Collect data before the prediction start date
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String dateStr = rs.getString("date");
            double totalAmount = rs.getDouble("total_amount");

            if (dateStr != null && totalAmount != 0) {
                Date date = dateFormat.parse(dateStr);
                double daysSinceReference = (date.getTime() - referenceDate.getTime()) / (1000 * 60 * 60 * 24);

                xData.add(daysSinceReference);
                yData.add(totalAmount);
            }
        }

        if (xData.isEmpty() || yData.isEmpty()) {
            jLabel5.setText("Insufficient historical data for prediction.");
            return;  // Avoid further processing if no data is available
        }

        // Normalize the data
        double[] normalizedX = normalize(xData);
        double[] normalizedY = normalize(yData);

        // Perform Gradient Descent to get the model parameters (theta)
        double[] theta = gradientDescent(normalizedX, normalizedY, 0.01, 10000);

        // Prepare to predict for future date range
        double totalPredicted = 0.0;  // Variable to hold the total predicted amount
        Date futureStartDate = dateChooser_from.getDate();
        Date futureEndDate = dateChooser_to.getDate();

        // Calculate number of days between future start and end dates
        long daysBetween = (futureEndDate.getTime() - futureStartDate.getTime()) / (1000 * 60 * 60 * 24);

        // Predict future totals for the entire date range
        for (long i = 0; i <= daysBetween; i++) {
            // Calculate days since reference for future prediction
            double futureDaysSinceReference = (futureStartDate.getTime() - referenceDate.getTime()) / (1000 * 60 * 60 * 24);
            double predictedTotal = predictAmount(theta, futureDaysSinceReference);

            // Add the predicted total to the overall total
            totalPredicted += predictedTotal;

            // Move to the next day
            futureStartDate = new Date(futureStartDate.getTime() + (1000 * 60 * 60 * 24));
        }

        // Display the total predicted amount for the range
        jLabel5.setText(String.format("Total Predicted Amount from %s to %s: %.2f", fromDateStr, toDateStr, totalPredicted));

    } catch (Exception e) {
        e.printStackTrace();
    }
}




// Normalize the data
public double[] normalize(List<Double> data) {
    double mean = data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    double stddev = Math.sqrt(data.stream().mapToDouble(val -> Math.pow(val - mean, 2)).average().orElse(0.0));

    if (stddev == 0) {
        System.out.println("Standard deviation is zero, returning zeros for normalization.");
        return new double[data.size()];
    }

    return data.stream().mapToDouble(val -> (val - mean) / stddev).toArray();
}

// Gradient Descent Implementation
public double[] gradientDescent(double[] x, double[] y, double alpha, int iterations) {
    int m = x.length;
    double[] theta = {0.0, 0.0}; // Initialize theta to zeros

    for (int iter = 0; iter < iterations; iter++) {
        double sum0 = 0, sum1 = 0;

        for (int i = 0; i < m; i++) {
            double prediction = theta[0] + theta[1] * x[i];
            sum0 += (prediction - y[i]);
            sum1 += (prediction - y[i]) * x[i];
        }

        theta[0] -= alpha * (1.0 / m) * sum0;
        theta[1] -= alpha * (1.0 / m) * sum1;

        // Optional: Monitor the cost function for convergence
        if (iter % 100 == 0) {
            double cost = computeCost(x, y, theta);
            System.out.println("Iteration: " + iter + ", Cost: " + cost);
        }

        // Check for NaN or Infinity
        if (Double.isNaN(theta[0]) || Double.isNaN(theta[1]) || Double.isInfinite(theta[0]) || Double.isInfinite(theta[1])) {
            System.out.println("Invalid theta values at iteration " + iter);
            break;
        }
    }

    return theta;
}

// Prediction function using the trained theta
public double predictAmount(double[] theta, double daysSinceReference) {
    return theta[0] + theta[1] * daysSinceReference; // Adjust this prediction logic as necessary
}

// Compute cost function for monitoring
public double computeCost(double[] x, double[] y, double[] theta) {
    int m = x.length;
    double totalCost = 0;

    for (int i = 0; i < m; i++) {
        double prediction = theta[0] + theta[1] * x[i];
        totalCost += Math.pow(prediction - y[i], 2);
    }

    return totalCost / (2 * m);
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
        dateChooser_from = new com.toedter.calendar.JDateChooser();
        dateChooser_to = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(124, 16, 52));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(dateChooser_from, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 130, 30));
        jPanel1.add(dateChooser_to, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, 130, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Select date To : ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 150, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("The expected fees to be collected is : ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        jPanel2.setBackground(new java.awt.Color(250, 171, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setText("Predict Fees");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/images/tutionsync white (2).png"))); // NOI18N
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 60, 60));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 90));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Select date from : ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Expected fees");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 660, 20));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Predict");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 100, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        predictFees();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(PredictFees11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PredictFees11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PredictFees11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PredictFees11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PredictFees11().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateChooser_from;
    private com.toedter.calendar.JDateChooser dateChooser_to;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
