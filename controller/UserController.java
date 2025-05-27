package controller;

public class UserController {
    public static boolean validateUser(String username, String password) {
        return UserDAO.validateUser(username, password);
    }

    public static boolean registerUser(String username, String password) {
        return UserDAO.registerUser(username, password);
    }
}