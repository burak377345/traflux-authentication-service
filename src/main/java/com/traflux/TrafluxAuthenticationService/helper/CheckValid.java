package com.traflux.TrafluxAuthenticationService.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckValid {
    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private static boolean isValidUserName(String userName) {
        String regex = "^[a-zA-Z0-9._\\-\\s]{3,}$"; // Kullanıcı adı en az 3 karakter uzunluğunda olmalı ve sadece a-z, A-Z, 0-9, ., _ veya - karakterlerini içermelidir.
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }
    private static boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*")
                && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
    public static boolean validateEmail(String email) {
        return isValidEmail(email);
    }
    public static boolean validateUserName(String userName) {
        return isValidUserName(userName);
    }
    public static boolean validatePassword(String password) {
        return isValidPassword(password);
    }
}
