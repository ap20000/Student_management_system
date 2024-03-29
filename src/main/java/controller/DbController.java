package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.LoginModel;
import model.PasswordEncryptionWithAes;
import model.StudentModel;
import util.StringUtil;

public class DbController {

    private static final String url = "jdbc:mysql://localhost:3306/college_app";
    private static final String user = "root";
    private static final String pass = "";
	private Object loginModel;

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);
    }

    public int getStudentLoginInfo(LoginModel loginModel) {
        try (Connection con = getConnection()) {
        	PreparedStatement st = getConnection()
	        		.prepareStatement(StringUtil.GET_LOGIN_STUDENT_INFO);

	        // Set the username in the first parameter of the prepared statement
	        st.setString(1, loginModel.getUsername());
	        
            
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
               
                String userDb = rs.getString("user_name");

          
               

                String encryptedPwd = rs.getString(StringUtil.password);
               

	            String decryptedPwd = PasswordEncryptionWithAes.decrypt(encryptedPwd, userDb);
              
                if (userDb.equals(( loginModel).getUsername()) 
	            		&& decryptedPwd.equals((loginModel).getPassword())) {
	                // Login successful, return 1
	                return 1;
	            } else {
	                // Username or password mismatch, return 0
	                return 0;
	            }
	        } else {
	            // Username not found in the database, return -1
	            return -1;
	        }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return -2;
        }
    }

    public int addStudent(StudentModel studentModel) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(StringUtil.INSERT_STUDENT)) {
            st.setString(1, studentModel.getUser_name());
            st.setString(2, studentModel.getFirst_name());
            st.setString(3, studentModel.getLast_name());
            st.setDate(4, Date.valueOf(studentModel.getDob()));
            st.setString(5, studentModel.getGender());
            st.setString(6, studentModel.getEmail());
            st.setString(7, studentModel.getPhone_number());
            st.setString(8, studentModel.getSubject());
            st.setString(9, PasswordEncryptionWithAes.encrypt(studentModel.getUser_name(),studentModel.getPassword()));

            int result = st.executeUpdate();
            
            if (result > 0) {
	            return 1;
	        } else {
	            return 0; 
	        }
        }catch (ClassNotFoundException | SQLException ex) {
	        
	        ex.printStackTrace();
	        return -1; 
	    }
    }
        
    
    public boolean checkUsernameIfExists(String user_name) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM student_info WHERE user_name = ?")) {
            st.setString(1, user_name);
            ResultSet rs = st.executeQuery();
            return rs.next(); 
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace(); 
            return false;
        }
    }
    public boolean checkEmailIfExists(String email) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM student_info WHERE email = ?")) {
            
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean checkNumberIfExists(String phone_number) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM student_info WHERE phone_number = ?")) {
            st.setString(1, phone_number);
            ResultSet rs = st.executeQuery();
            return rs.next(); 
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace(); 
            return false;
        }
    }

	

    public String getFirstName(String username) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement("SELECT first_name FROM student_info WHERE user_name = ?")) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("first_name");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLastName(String username) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement("SELECT last_name FROM student_info WHERE user_name = ?")) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("last_name");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

	
    	

        
}
        
