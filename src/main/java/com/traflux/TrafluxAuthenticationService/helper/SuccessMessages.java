package com.traflux.TrafluxAuthenticationService.helper;

public class SuccessMessages {
    private static final String SUCCESS_LOGIN = "Kullanıcı Başarıyla Giriş Yaptı";

    private static final String SUCCESS_REGISTER = "Kullanıcı Başarıyla Kayıt Oldu";

    private static final String SUCCESS_REFRESHED_TOKEN = "Erişim Tokeni Başarıyla Yenilendi";

    public static String getSuccessLogin() {
        return SUCCESS_LOGIN;
    }

    public static String getSuccessRegister() {
        return SUCCESS_REGISTER;
    }

    public static String getSuccessRefreshedToken() {
        return SUCCESS_REFRESHED_TOKEN;
    }

}
