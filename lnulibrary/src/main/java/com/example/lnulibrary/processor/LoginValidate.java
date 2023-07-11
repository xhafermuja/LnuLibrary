package com.example.lnulibrary.processor;

public class LoginValidate {
    public static boolean validate(String personalNumber,String password) {
        if (personalNumber.equals("") || personalNumber.equals(" ") || password.equals("") || password.equals(" ") ) {
            return true;
        }
        if (personalNumber == null || password == null) {
            return true;
        }

        return false;
    }
}
