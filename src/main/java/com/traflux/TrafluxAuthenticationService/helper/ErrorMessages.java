package com.traflux.TrafluxAuthenticationService.helper;

public class ErrorMessages {
    private static final String USER_NOT_FOUND = "Kullanıcı Bulunamadı";

    private static final String INVALID_EMAIL = "Lütfen Geçerli Bir E-posta Giriniz";

    private static final String INVALID_USER_NAME = "Kullanıcı Adı Yalnızca Harflerden, " +
            "Rakamlardan, Belirli Özel Karakterlerden Oluşmalıdır ve " +
            "En Az 3 Karakter Uzunluğunda Olmalıdır.";

    private static final String INVALID_PASSWORD = "Şifre En Az 8 Karakterden Oluşmalı, " +
            "En Az Bir Büyük Harf ve En Az Bir Özel Karakter İçermelidir";

    private static final String INVALID_TOKEN = "Yenileme Tokeni Geçersiz veya Bulunamadı";

    private static final String INVALID_CREDENTIALS = "Geçersiz E-posta veya Şifre";

    private static final String USER_NAME_EXISTS = "Kullanıcı Adı Zaten Kullanılıyor";

    private static final String EMAIL_EXISTS = "E-posta Zaten Kullanılıyor";

    private static final String LOGGED_ACCOUNT_NOT_FOUND = "Giriş Yapılmış Hesap Bulunamadı. Lütfen Tekrar Giriş Yapınız.";

    private static final String LOGGED_ACCOUNT_FAILED = "Giriş Yapılan Hesap Bulunurken Hata Oluştu. Lütfen Tekrar Giriş Yapınız.";

    private static final String LOGIN_FAILED = "Giriş Sırasında Bir Hata Oluştu";

    private static final String REGISTER_FAILED = "Kayıt Sırasında Bir Hata Oluştu";

    public static String getUserNotFound() {
        return USER_NOT_FOUND;
    }

    public static String getInvalidEmail() {
        return INVALID_EMAIL;
    }

    public static String getInvalidUserName() {
        return INVALID_USER_NAME;
    }

    public static String getInvalidPassword() {
        return INVALID_PASSWORD;
    }

    public static String getInvalidToken() {
        return INVALID_TOKEN;
    }

    public static String getInvalidCredentials() { return INVALID_CREDENTIALS; }

    public static String getUserNameExists() {
        return USER_NAME_EXISTS;
    }

    public static String getEmailExists() {
        return EMAIL_EXISTS;
    }

    public static String getLoggedAccountNotFound() {
        return LOGGED_ACCOUNT_NOT_FOUND;
    }

    public static String getLoggedAccountFailed() {
        return LOGGED_ACCOUNT_FAILED;
    }

    public static String getLoginFailed() { return LOGIN_FAILED; }

    public static String getRegisterFailed() { return REGISTER_FAILED; }
}
