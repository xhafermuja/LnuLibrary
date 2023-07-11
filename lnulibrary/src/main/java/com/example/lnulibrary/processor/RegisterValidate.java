package com.example.lnulibrary.processor;

import com.example.lnulibrary.components.ErrorPopupComponent;

import java.util.ArrayList;
import java.util.List;

public class RegisterValidate {
    public  static boolean validate(String name, String role, String personalNumber, String password) {

        ArrayList<String> data = new ArrayList<>(List.of(name, role, personalNumber, password));

        for (String value : data) {
            if (value == null || value.equals("") || value.equals(" ") || value.length() == 0) {
                ErrorPopupComponent.show("Empty fields");
                return true;
            }

        }

        if (personalNumber.length() <6) {
            ErrorPopupComponent.show("Invalid personal ID");
            return true;
        }
        if(password.length() < 8 )
        {
            ErrorPopupComponent.show("Password must be 8 characters");
            return true;
        }
        return false;
    }

}
