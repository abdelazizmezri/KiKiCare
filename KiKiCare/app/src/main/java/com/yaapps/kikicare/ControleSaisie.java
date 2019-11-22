package com.yaapps.kikicare;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControleSaisie {

    private static Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String pwd=  "^[A-Za-z0-9]{6,}$";
    private static Pattern pattern1 = Pattern.compile(pwd);

    public static boolean isString(String text) {
        return text.matches("^[a-zA-Z]+$");
    }

    public static boolean isUsername(String text) {
        return text.matches("^[A-Za-z]+$+");

    }

    /*public static boolean isUsername(String text) {
        return text.matches("^[A-Za-z0-9]+$+");

    }*/

    public static boolean DateNullCS(String date){
        return date.equals("");
    }

    public static boolean adresse(String text) {
        return text.matches("^[A-Z a-z 0-9]+$");
    }

    public static boolean isCin(String text) {
        return text.matches("^[0-9]+$") && text.length() == 8;
    }

    public static boolean isTel(String text) {
        return text.matches("^[0-9]+$") && text.length() == 8;
    }

    public static boolean validEmail(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static boolean validPasswor(final String hex) {
        matcher = pattern1.matcher(hex);
        return matcher.matches();
    }

}