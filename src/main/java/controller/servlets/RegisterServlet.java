package controller.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.DbController;
import model.StudentModel;
import util.MyDataValidator;
import util.StringUtil;

@WebServlet(asyncSupported = true, urlPatterns ="/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    DbController dbController = new DbController();
    
    public RegisterServlet() {
    	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	
        
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user_name = request.getParameter(StringUtil.user_name);
        String first_name = request.getParameter(StringUtil.first_name);
        String last_name = request.getParameter(StringUtil.last_name);
        String dobString = request.getParameter(StringUtil.dobString);
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobString);
        } catch (DateTimeParseException e) {
            // Handle the case where dobString is not in the expected format
            // You can redirect the user back to the registration form with an error message
            response.sendRedirect(request.getContextPath() + "./pages/register.html?error=invalid_date_format");
            return; // Exit the method
        }
        String gender = request.getParameter(StringUtil.gender);
        String email = request.getParameter(StringUtil.email);
        String phone_number = request.getParameter(StringUtil.phone_number);
        String subject = request.getParameter(StringUtil.subject);
        String password = request.getParameter(StringUtil.password);
        String retype_password = request.getParameter(StringUtil.retype_password);
        
     // Validate first name and last name format
        if (!MyDataValidator.validateNameFormat(first_name) ) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Invalid first name ");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }
        if (!MyDataValidator.validateNameFormat(last_name)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Invalid  last name ");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        // Validate username length and format
        if (user_name.length() < 6 || !MyDataValidator.validateUsernameFormat(user_name)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Username must be at least 6 characters long and should not contain special characters.");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        // Validate birthday date restriction
        if (dob.isAfter(LocalDate.now())) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Invalid birthday date.");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        // Validate phone number format
        if (!MyDataValidator.validatePhoneNumberFormat(phone_number)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Invalid phone number format.");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        // Validate password complexity
        if (!MyDataValidator.validatePasswordComplexity(password)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Password must be at least 6 characters ");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        

        if (dbController.checkUsernameIfExists(user_name)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Username already exists.");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        if (dbController.checkEmailIfExists(email)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Email already exists.");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }

        if (dbController.checkNumberIfExists(phone_number)) {
            request.setAttribute(StringUtil.MESSAGE_ERROR, "Phone number already exists.");
            request.getRequestDispatcher(StringUtil.PAGE_URL_REGISTER).forward(request, response);
            return;
        }
//        

     

        
        

        StudentModel studentModel = new StudentModel(user_name, first_name, last_name, dob, gender, email, phone_number, subject, password);
        int result = dbController.addStudent(studentModel);
        

        if (result == 1) {
        	
        	request.setAttribute (StringUtil.MESSAGE_SUCCESS_REGISTER, StringUtil.MESSAGE_SUCCESS_REGISTER);
        	response.sendRedirect(request.getContextPath() + StringUtil.PAGE_URL_LOGIN);
        }  else {
        	request.setAttribute (StringUtil.MESSAGE_ERROR, StringUtil. MESSAGE_ERROR_SERVER);
        	request.getRequestDispatcher (StringUtil.PAGE_URL_REGISTER).forward(request, response);
        }
    }

}
