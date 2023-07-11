package com.example.lnulibrary;

import com.example.lnulibrary.Member;
import com.example.lnulibrary.components.ErrorPopupComponent;
import com.example.lnulibrary.components.SuccessPopupComponent;
import com.example.lnulibrary.processor.RegisterValidate;
import com.example.lnulibrary.processor.SecurityHelper;
import com.example.lnulibrary.repositories.MemberRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField personalNumberField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button registerBtn;
    @FXML
    private Label goBackToLogin;

    private Logger logger=Logger.getLogger(RegisterController.class);

    @FXML
    void onRegisterAction(ActionEvent event) {

        try {
            String name = nameField.getText();
            String role = roleField.getText();
            String personalNumber = personalNumberField.getText();
            String password = passwordField.getText();

            boolean emptyFields = RegisterValidate.validate(name, role, personalNumber, password);
            if (emptyFields)
                return;

            boolean userExists = MemberRepository.find(personalNumber,name);
            if (userExists) {
                ErrorPopupComponent.show("User already exists");
                return;
            }

            boolean userSuspended = MemberRepository.findSuspended(personalNumber);
            if(userSuspended) {
                ErrorPopupComponent.show("You can not register because you have been suspended from using the library.");
                return;
            }

            Member registeredMember = register(name, role, personalNumber, password);

            if (registeredMember != null) {
                SuccessPopupComponent.show("User is registered " , "Registered");
                return;
            } else {
                ErrorPopupComponent.show("User was not registered");
                return;
            }

        } catch (Exception ex) {
            System.out.println(ex);
            ErrorPopupComponent.show(ex);
        }

        logger.info("User with ID "+SessionManager.member.getId()+" has been registered in");

    }

    Member register(String name, String role, String personalNumber, String password) throws Exception {
        Member member = new Member();
        member.setIsActive(true);
        member.setPersonalNumber(personalNumber);
        member.setName(name);
        member.setRole(role);
        String salt = SecurityHelper.generateSalt();
        String hashedPassword = SecurityHelper.computeHash(password, salt);
        member.setPassword(hashedPassword);
        member.setSalt(salt);
        if(MemberRepository.create(member) != null) {
            return member;
        }
        return null;
    }

    @FXML
    private void onBackToLoginAction(MouseEvent e) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);


    }
}
