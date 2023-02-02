// Team Members:
// Team member 1: Akhil Parimi – B00866489 (CS532-02)
// Team member 2: Nagalakshmi Prasanna Pujita Bodapati – B00929285 (CS532-02)

// We have done this assignment completely on our own except for the tools/software acknowledged in
// the project report. We have not copied it, nor have we given our solution to anyone else.We understand
// that if we are involved in plagiarism or cheating, we will have to sign an official form that we have
// cheated and that this form will be stored in our official university records. We also understand that
// we will receive a grade of 0 for the involved assignment and our grades will be reduced by one level
// (e.g., from A to A- or from B+ to B) for our first offense, and that we will receive a grade of “F”
// for the course for any additional offense of any kind.

// Akhil Parimi
// Pujita Bodapati

import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import java.util.Formatter;
import oracle.jdbc.pool.OracleDataSource;
import java.util.Scanner;


class DBSProj {
    public static void main(String args[]) {
        // To read all the inputs that are given from the console.
        Scanner scannerObj = new Scanner(System.in);

        try {
            // The following are the username and password that are required to connect to Oracle Database.
            String username = "enter the username";
            String password = "enter the sql pwd";

            // We created a connection object
            OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
            Connection connObj = ds.getConnection(username, password);
            CallableStatement cs = null;
            ResultSet rs;
            BufferedReader readKeyBoard;

            boolean outerLoop = true, dispLoop = true;
            int maonOpt, subOpt;
            String stdid, dept, course, clsid;

            while (outerLoop) {
                dispLoop = true;
                // The following are the options required for main menu.
                System.out.println("");
                System.out.println("Please select any one option from the following:");
                System.out.println(
                        " 1. Display existing table Data\n 2. Get student details for a given Class by classid");
                System.out.println(" 3. Course prerequisites by dept_code and course#");
                System.out.println(" 4. Enroll a graduate student\n 5. Drop a graduate student enrollment");
                System.out.println(" 6. Delete a student\n 0. Exit");
                maonOpt = scannerObj.nextInt();

                switch (maonOpt) {
                    case 1:
                        while (dispLoop) {
                            // The following are the sub-options required for displaying all the tables in the database.
                            System.out.println("Select a table to display the records.");
                            System.out.println(" 1. Students Table\n 2. Courses Table\n 3. Prerequisites Table\n 4. Classes Table");
                            System.out.println(
                                    " 5. Enrollments Table\n 6. Logs Table\n 7.Score_Grade Table\n 8. Course_Credits Table \n 0. Return to main options");

                            subOpt = scannerObj.nextInt();
                            switch (subOpt) {
                                case 1:
                                //Student headers
                                String[] studentHeaders = {"B#", "FIRST_NAME", "LAST_NAME", "ST_LEVEL", "GPA", "EMAIL", "BDATE"};
                                    // To call a written procedure included in the main package:
                                    cs = connObj
                                            .prepareCall("begin student_reg_system.display_all_students(?); end;");
                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = ((OracleCallableStatement) cs).getCursor(1);
                                    // Print the selected table contents.
                                    System.out.println(
                                            "--------------------------------------------------------------------------------------------------------------------------------------------");
                                    System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", studentHeaders[0], studentHeaders[1], studentHeaders[2], studentHeaders[3], studentHeaders[4], studentHeaders[5], studentHeaders[6]);
                                    System.out.println(
                                            "--------------------------------------------------------------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", rs.getString(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4),
                                                rs.getDouble(5),
                                                rs.getString(6),
                                                rs.getString(7));
                                    }
                                    break;

                                case 2:
                                    // To call a written procedure included in the main package:
                                    cs = connObj
                                            .prepareCall("begin student_reg_system.display_all_courses(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);
                                    //Course headers
                                    String[] courseHeaders = {"DEPT", "COURSE#", "TITLE"};
                                    // Print the selected contents of table.
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");
                                            System.out.format("%-15s %-15s %-15s%n", courseHeaders[0], courseHeaders[1], courseHeaders[2]);
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s %-15s%n", rs.getString(1),
                                        rs.getString(2),
                                        rs.getString(3));
                                    }
                                    break;

                                case 3:
                                    // To call a written procedure included in the main package:
                                    cs = connObj.prepareCall(
                                            "begin student_reg_system.display_all_prerequisites(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);
                                    //Prerequisites Headers
                                    String[] prerequisitesHeaders = {"DEPT_CODE", "COURSE#", "PRE_DEPT_CODE", "PRE_COURSE#"};
                                    // Print the selected contents of table.
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");
                                            System.out.format("%-15s %-15s %-15s %-15s%n", prerequisitesHeaders[0], prerequisitesHeaders[1], prerequisitesHeaders[2], prerequisitesHeaders[3]);
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s %-15s %-15s%n", rs.getString(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4));
                                    }
                                    break;

                                case 4:
                                    // To call a written procedure included in the main package:
                                    cs = connObj
                                            .prepareCall("begin student_reg_system.display_all_classes(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);
                                    //Classes headers
                                    String[] classesHeaders = {"CLASSID", "DEPT_CODE", "COURSE#", "SECT_NO", "YEAR", "SEMESTER", "LIMIT", "CLASS_SIZE"};
                                    // Print the selected contents of table.
                                    System.out.println(
                                            "--------------------------------------------------------------------------------------------------------------------------------");
                                            System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", classesHeaders[0], classesHeaders[1], classesHeaders[2], classesHeaders[3], classesHeaders[4], classesHeaders[5], classesHeaders[6], classesHeaders[7]);
                                    System.out.println(
                                            "--------------------------------------------------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", rs.getString(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4),
                                                rs.getString(5),
                                                rs.getString(6),
                                                rs.getString(7),
                                                rs.getString(8));
                                    }
                                    break;

                                case 5:
                                    // To call a written procedure included in the main package:
                                    cs = connObj.prepareCall(
                                            "begin student_reg_system.display_all_g_enrollments(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);

                                    // Print the selected contents of table.
                                    //G_Enrollments Header
                                    String[] gEnrollmentsHeader = {"G_B#", "CLASSID", "SCORE"};
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");
                                            System.out.format("%-15s %-15s %-15s%n", gEnrollmentsHeader[0], gEnrollmentsHeader[1], gEnrollmentsHeader[2]);
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s %-15s%n", rs.getString(1),
                                                rs.getString(2),
                                                rs.getString(3));
                                    }
                                    break;

                                case 6:
                                    // To call a written procedure included in the main package:
                                    cs = connObj.prepareCall("begin student_reg_system.display_all_logs(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);

                                    // Print the selected contents of table.
                                    //Logs Headers
                                    String[] logsHeaders = {"LOGID", "USER", "TIME", "TABLE_NAME", "OPERATION", "KEY_VALUE"};
                                    System.out.println(
                                            "-----------------------------------------------------------------------------------------------------------");
                                            System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", logsHeaders[0], logsHeaders[1], logsHeaders[2], logsHeaders[3], logsHeaders[4], logsHeaders[5]);
                                    System.out.println(
                                            "-----------------------------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s%n", rs.getString(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4),
                                                rs.getString(5),
                                                rs.getString(6));
                                    }
                                    break;
                                case 7:
                                    // To call a written procedure included in the main package:
                                    cs = connObj.prepareCall(
                                            "begin student_reg_system.display_all_score_grade(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);

                                    // Print the selected contents of table.
                                    //Score Grade Headers
                                    String[] scoreHeaders = {"Score", "LGRADE"};
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");
                                            System.out.format("%-15s %-15s%n", scoreHeaders[0], scoreHeaders[1]);
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s%n", rs.getString(1),
                                                rs.getString(2));
                                    }
                                    break;
                                case 8:
                                    // To call a written procedure included in the main package:
                                    cs = connObj.prepareCall(
                                            "begin student_reg_system.display_all_course_credits(?); end;");

                                    // setting the return type to the procedure.
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);

                                    // To execute the above procedure.
                                    cs.execute();

                                    // To read the cursor.
                                    rs = (ResultSet) cs.getObject(1);

                                    // Print the selected contents of table.
                                    //Course Credits Headers
                                    String[] courseCreditsHeaders = {"Course#", "Credits"};
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");
                                            System.out.format("%-15s %-15s%n", courseCreditsHeaders[0], courseCreditsHeaders[1]);
                                    System.out.println(
                                            "------------------------------------------------------------------------------------");

                                    while (rs.next()) {
                                        System.out.format("%-15s %-15s%n", rs.getString(1),
                                                rs.getString(2));
                                    }
                                    break;
                                case 0:
                                    dispLoop = false;
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                                    break;
                            }
                        }
                        break;

                    case 2:
                        // Read data from user to insert the details into table.
                        readKeyBoard = new BufferedReader(new InputStreamReader(System.in));

                        System.out.print("Please Enter CLASSID:");
                        clsid = readKeyBoard.readLine();

                        // To call a written procedure included in the main package:
                        cs = connObj.prepareCall("begin student_reg_system.get_stud_info_by_class(:1,:2,:3); end;");

                        // Set in parameters to procedure.
                        cs.setString(1, clsid);
                        cs.registerOutParameter(2, Types.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        // To execute the above procedure.
                        cs.executeQuery();

                        rs = ((OracleCallableStatement) cs).getCursor(3);
                        if (cs.getString(2) != null) {
                            System.out.println(cs.getString(2));
                        } else {
                                //Student Info headers
                                String[] studentInfoHeaders = {"B#", "FIRST_NAME", "LAST_NAME"};
                            // Print the selected contents of table.
                            System.out.println(
                                    "------------------------------------------------------------------------------------");
                            System.out.format("%-15s %-15s %-15s%n", studentInfoHeaders[0], studentInfoHeaders[1], studentInfoHeaders[2]);
                            System.out.println(
                                    "------------------------------------------------------------------------------------");

                            while (rs.next()) {
                                System.out.format("%-15s %-15s %-15s%n", rs.getString(1),
                                        rs.getString(2),
                                        rs.getString(3));
                            }
                        }
                        break;

                    case 3:
                         // Read user input data and pass them as input parameters to procedure.
                         readKeyBoard = new BufferedReader(new InputStreamReader(System.in));

                         System.out.print("Please Enter Department Code:");
                         dept = readKeyBoard.readLine();
                         System.out.print("Please Enter Course Number:");
                         course = readKeyBoard.readLine();
 
                         // To call a written procedure included in the main package:
                         cs = connObj
                                 .prepareCall(
                                         "begin student_reg_system.get_prereq_info_by_dept_course(:1,:2,:3,:4); end;");
 
                         // Set in and out parameters.
                         cs.setString(1, dept);
                         cs.setString(2, course);
                         cs.registerOutParameter(3, Types.VARCHAR);
                         cs.registerOutParameter(4, OracleTypes.CURSOR);
 
                         // To execute the above procedure.
                         cs.execute();
 
                         // To read the cursor.
                         rs = ((OracleCallableStatement) cs).getCursor(4);
                         if (cs.getString(3) != null) {
                             System.out.println(cs.getString(3));
                         } else {
                             // Print the selected contents of table.
                             System.out.println(
                                     "------------------------------------------------------------------------------------");
                             System.out.println("PREREQUISITE_COURSE");
                             System.out.println(
                                     "------------------------------------------------------------------------------------");
 
                             while (rs.next()) {
                                 System.out.println(rs.getString(2));
                             }
                         }
 
                         break;

                    case 4:
                       // Read user input data and pass to procedure as input parameters.
                       readKeyBoard = new BufferedReader(new InputStreamReader(System.in));

                       System.out.print("Please Enter B#:");
                       stdid = readKeyBoard.readLine();
                       System.out.print("Please Enter CLASSID:");
                       clsid = readKeyBoard.readLine();

                       // To call a written procedure included in the main package:
                       cs = connObj.prepareCall("begin student_reg_system.enroll_graduate_students(:1,:2,:3); end;");

                       // set in and out parameters to procedure.
                       cs.setString(1, stdid);
                       cs.setString(2, clsid);
                       cs.registerOutParameter(3, Types.VARCHAR);

                       // To execute the above procedure.
                       cs.execute();

                       // Write message.
                       System.out.println(cs.getString(3));
                       break;


                    case 5:
                      // Read user input data and pass to procedure as input parameters.
                      readKeyBoard = new BufferedReader(new InputStreamReader(System.in));

                      System.out.print("Please Enter B#:");
                      stdid = readKeyBoard.readLine();
                      System.out.print("Please Enter CLASSID:");
                      clsid = readKeyBoard.readLine();

                      // To call a written procedure included in the main package:
                      cs = connObj.prepareCall("begin student_reg_system.drop_graduate_student(:1,:2,:3); end;");

                      // Set in and out paramters.
                      cs.setString(1, stdid);
                      cs.setString(2, clsid);
                      cs.registerOutParameter(3, Types.VARCHAR);

                      // Execute the procedure.
                      cs.execute();

                      // Write message.
                      System.out.println(cs.getString(3));
                      break;

                    case 6:
                        // Read user input data and pass to procedure as input data
                        readKeyBoard = new BufferedReader(new InputStreamReader(System.in));

                        System.out.print("Please Enter B#:");
                        stdid = readKeyBoard.readLine();

                        // To call a written procedure included in the main package:
                        cs = connObj.prepareCall("begin student_reg_system.delete_curr_student(:1,:2); end;");

                        // Set in and out parameters.
                        cs.setString(1, stdid);
                        cs.registerOutParameter(2, Types.VARCHAR);

                        // Execute the procedure.
                        cs.execute();

                        // Print the message.
                        System.out.println(cs.getString(2));
                        break;
                    case 0:
                        outerLoop = false;
                        break;

                    default:
                        System.out.println("Invalid option.");
                        break;
                }

            }

            cs.close();
            connObj.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n" + e.getMessage());
        }
    }
}