package util;

public class MyDataValidator {
    
    // Other existing validation methods
    
    public static boolean validateNameFormat(String name) {
        String nameRegex = "^[A-Za-z ]*$";
        return name.matches(nameRegex);
    }
    
    public static boolean validateUsernameFormat(String username) {
        String usernameRegex = "^[a-zA-Z0-9]{6,}$";
        return username.matches(usernameRegex);
    }
    
    public static boolean validatePhoneNumberFormat(String phoneNumber) {
        // Allow either +[10 digits] or [10 digits]
        String phoneRegex = "^(\\+[0-9]{10}|[0-9]{10})$";
        return phoneNumber.matches(phoneRegex);
    }

    
    public static boolean validatePasswordComplexity(String password) {
        // Password should have exactly 6 characters
        String passwordRegex = "^.{6}$";
        return password.matches(passwordRegex);
    }
    
    
}
