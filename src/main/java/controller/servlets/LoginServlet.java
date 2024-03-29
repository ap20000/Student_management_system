package controller.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.DbController;
import model.LoginModel;
import util.MyDataValidator;
import util.StringUtil;

@WebServlet(urlPatterns = "/LoginServlet", asyncSupported = true)
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    DbController dbController = new DbController();

    public LoginServlet() {
    	this.dbController = new DbController();
    }
    
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        LoginModel loginModel = new LoginModel(username, password);

        // Call DBController to validate login credentials
        int loginResult = dbController.getStudentLoginInfo(loginModel);

        
        
        
        if (loginResult == 1) {
            // Login successful
        	HttpSession userSession = request.getSession();
			userSession.setAttribute("username", username);
			userSession.setMaxInactiveInterval(30*30);
			
			Cookie userCookie= new Cookie("user", username);
			userCookie.setMaxAge(30*60);
			response.addCookie(userCookie);
			
            request.setAttribute(StringUtil.MESSAGE_SUCCESS, StringUtil.MESSAGE_SUCCESS_LOGIN);
            response.sendRedirect(request.getContextPath() + StringUtil.PAGE_URL_WELCOME);
        } else if (loginResult == 0) {
            // Username or password mismatch
            request.setAttribute(StringUtil.MESSAGE_ERROR, StringUtil.MESSAGE_ERROR_LOGIN);
            request.getRequestDispatcher(StringUtil.PAGE_URL_LOGIN).forward(request, response);
        } else if (loginResult == -1) {
            // Username not found
            request.setAttribute(StringUtil.MESSAGE_ERROR, StringUtil.MESSAGE_ERROR_CREATE_ACCOUNT);
            request.getRequestDispatcher(StringUtil.PAGE_URL_LOGIN).forward(request, response);
        } else {
            // Internal server error
            request.setAttribute(StringUtil.MESSAGE_ERROR, StringUtil.MESSAGE_ERROR_SERVER);
            request.getRequestDispatcher(StringUtil.PAGE_URL_LOGIN).forward(request, response);
        }
    }

}
