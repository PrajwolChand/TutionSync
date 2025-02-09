/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fees_management_system;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class InsertFeesRecords1 {

    public static void insertRecords(Connection con) {
        String sql = "INSERT INTO predictiontest (reciept_no, student_name, roll_no, payment_mode, cheque_no, bank_name, esewa_no, transaction_code, course_name, total_amount, date, amount, total_in_words, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String[] nepaliNames = {"Ram", "Shyam", "Hari", "Gita", "Sita", "Rita", "Kiran", "Sunil", "Anil", "Binod"};
            String[] courses = {"Java", "C#", "Python", "Linux Training", "Laravel", "Basic IT Training", "Wordpress"};
            float[] courseFees = {25000.0f, 10000.0f, 10000.0f, 4000.0f, 15000.0f, 2500.0f, 15000.0f};

            // Define the date range
            LocalDate startDate = LocalDate.parse("2010-01-01");
            LocalDate endDate = LocalDate.parse("2024-09-26");

            // Calculate total number of days in the date range
            long totalDays = ChronoUnit.DAYS.between(startDate, endDate);

            int recieptNo = 1;  // Initialize receipt number

            // Iterate over every day in the date range
            for (long day = 0; day <= totalDays; day++) {
                LocalDate currentDate = startDate.plusDays(day);
                Date sqlDate = Date.valueOf(currentDate);

                // Determine how far along the timeline we are (0.0 at the start, 1.0 at the end)
                float timelineFactor = (float) day / totalDays;

                // Decide how many payments will happen on this day (at least 1)
                int numberOfPayments = 1 + ThreadLocalRandom.current().nextInt(0, 3);  // Between 1 and 3 payments per day
                
                for (int j = 0; j < numberOfPayments; j++) {
                    String studentName = nepaliNames[recieptNo % nepaliNames.length] + recieptNo;
                    String rollNo = "RollNo" + recieptNo;
                    String paymentMode = "Cash";  // Can add more modes randomly
                    String chequeNo = "";
                    String bankName = "";
                    String esewaNo = "";
                    String transactionCode = "";

                    // Select a random course
                    int courseIndex = recieptNo % courses.length;
                    String courseName = courses[courseIndex];
                    float totalAmount = courseFees[courseIndex];

                    // Increasing amount paid based on the timelineFactor (payment trend logic)
                    float amountPaid = totalAmount * (0.2f + 0.8f * timelineFactor);  // Starting at 20%, increasing to 100%

                    // Ensure the amount does not exceed the totalAmount
                    amountPaid = Math.min(amountPaid, totalAmount);

                    String totalInWords = convertAmountToWords(amountPaid);
                    String remark = amountPaid < totalAmount ? "Partial payment" : "Full payment";

                    // Set values in PreparedStatement
                    pst.setInt(1, recieptNo);  // reciept_no
                    pst.setString(2, studentName);  // student_name
                    pst.setString(3, rollNo);  // roll_no
                    pst.setString(4, paymentMode);  // payment_mode
                    pst.setString(5, chequeNo);  // cheque_no
                    pst.setString(6, bankName);  // bank_name
                    pst.setString(7, esewaNo);  // esewa_no
                    pst.setString(8, transactionCode);  // transaction_code
                    pst.setString(9, courseName);  // course_name
                    pst.setFloat(10, totalAmount);  // total_amount
                    pst.setDate(11, sqlDate);  // date
                    pst.setFloat(12, amountPaid);  // amount
                    pst.setString(13, totalInWords);  // total_in_words
                    pst.setString(14, remark);  // remark

                    pst.addBatch();  // Add to batch

                    recieptNo++;  // Increment receipt number
                }
            }

            pst.executeBatch();  // Execute the batch
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Conversion method from amount to words
    public static String convertAmountToWords(float amount) {
        int number = (int) amount;
        String[] units = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        
        if (number == 0) {
            return "Zero";
        }
        
        if (number < 20) {
            return units[number];
        } else if (number < 100) {
            return tens[number / 10] + (number % 10 != 0 ? " " + units[number % 10] : "");
        } else if (number < 1000) {
            return units[number / 100] + " Hundred" + (number % 100 != 0 ? " and " + convertAmountToWords(number % 100) : "");
        } else if (number < 100000) {
            return convertAmountToWords(number / 1000) + " Thousand" + (number % 1000 != 0 ? " " + convertAmountToWords(number % 1000) : "");
        } else {
            return "Amount too large to convert";
        }
    }

    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();  // Assuming DBConnection is your utility for DB connection
            insertRecords(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

