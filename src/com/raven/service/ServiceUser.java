package com.raven.service;

import com.raven.connection.DatabaseConnection;
import com.raven.model.ModelLogin;
import com.raven.model.ModelUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;

public class ServiceUser {

    private final Connection con;

    public ServiceUser() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public ModelUser login(ModelLogin login) {
        ModelUser data = null;

        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT UserID, UserName, Email FROM user WHERE Email = ? AND Password = ? AND Status = 'Verified' LIMIT 1;")) {
            preparedStatement.setString(1, login.getEmail());
            preparedStatement.setString(2, login.getPassword());

            try (ResultSet r = preparedStatement.executeQuery()) {
                if (r.next()) {
                    int userID = r.getInt("UserID");
                    String userName = r.getString("UserName");
                    String email = r.getString("Email");
                    data = new ModelUser(userID, userName, email, "");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * @param user
     * @throws SQLException
     */
    public void insertUser(ModelUser user) throws SQLException {
        PreparedStatement p = null;
        ResultSet r = null;
    
        try {
            String code = generateVerifyCode();
            String sqlString = "INSERT INTO user (UserName, Email, Password, VerifyCode, Status, Age, Gender, Weight) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            System.out.println("Executing SQL query: " + sqlString);
            System.out.println("Parameters: " + user.getUserName() + ", " + user.getEmail() + ", " + user.getPassword() + ", " + code);
    
            p = con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
    
            p.setString(1, user.getUserName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getPassword());
            p.setString(4, code);
            p.setString(5, "Pending");
    
            if (user.getAge() != null) {
                p.setInt(6, user.getAge());
            } else {
                p.setNull(6, java.sql.Types.INTEGER);
            }
    
            if (user.getGender() != null && !user.getGender().isEmpty()) {
                p.setString(7, user.getGender());
            } else {
                p.setNull(7, java.sql.Types.VARCHAR);
            }
    
            if (user.getWeight() != null) {
                p.setInt(8, user.getWeight());
            } else {
                p.setNull(8, java.sql.Types.INTEGER);
            }
    
            p.executeUpdate();
            r = p.getGeneratedKeys();
    
            if (r.next()) {
                int userID = r.getInt(1);
                System.out.println("Generated UserID: " + userID);
                user.setUserID(userID);
            }
    
            user.setVerifyCode(code);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (r != null) {
                r.close();
            }
            if (p != null) {
                p.close();
            }
        }
    }
    
    
    

    private String generateVerifyCode() throws SQLException {
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        String code;

        do {
            code = df.format(ran.nextInt(1000000));
        } while (checkDuplicateCode(code));

        return code;
    }

    private boolean checkDuplicateCode(String code) throws SQLException {
        boolean duplicate = false;

        try (PreparedStatement p = con.prepareStatement("SELECT UserID FROM user WHERE VerifyCode = ? LIMIT 1;")) {
            p.setString(1, code);

            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    duplicate = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return duplicate;
    }

    public boolean checkDuplicateUser(String user) {
        boolean duplicate = false;

        try (PreparedStatement p = con.prepareStatement("SELECT UserID FROM user WHERE Email = ? AND Status = 'Verified' LIMIT 1;")) {
            p.setString(1, user);

            try (ResultSet r = p.executeQuery()) {
                duplicate = r.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return duplicate;
    }

    public boolean checkDuplicateEmail(String email) throws SQLException {
        boolean duplicate = false;

        try (PreparedStatement p = con.prepareStatement("SELECT UserID FROM user WHERE Email = ? AND Status = 'Verified' LIMIT 1;")) {
            p.setString(1, email);

            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    duplicate = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return duplicate;
    }

    public void doneVerify(int userID) throws SQLException {
        String sql = "UPDATE user SET Status = 'Verified' WHERE UserID = ?;";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.executeUpdate();
        }
    }

    public boolean verifyCodeWithUser(int userID, String code) throws SQLException {
    String sql = "SELECT UserID FROM user WHERE UserID = ? AND VerifyCode = ? LIMIT 1;";
    
    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
        preparedStatement.setInt(1, userID);
        preparedStatement.setString(2, code);

        System.out.println("Executing SQL query: " + sql);
        System.out.println("User ID: " + userID + ", Verification Code: " + code);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Before executing query");
            if (resultSet.next()) {
                // Verification successful, update status to 'Verified'
                doneVerify(userID);

                System.out.println("After executing query");
                System.out.println("Verification successful for user ID: " + userID);
                return true;
            } else {
                System.out.println("After executing query");
                System.out.println("Verification failed for user ID: " + userID);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // handle the exception appropriately
        }
    }
}}