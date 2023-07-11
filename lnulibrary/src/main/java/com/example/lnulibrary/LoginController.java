package com.example.lnulibrary;

import com.example.lnulibrary.components.ErrorPopupComponent;
import com.example.lnulibrary.controllers.MainViewController;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.processor.LoginProcessor;
import com.example.lnulibrary.processor.LoginValidate;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.Logger;

public class LoginController {
    @FXML
    private AnchorPane parent;
    @FXML
    private Pane content_area;
    @FXML
    private TextField personalNumberField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private Label goToRegister;
    @FXML
    private Label newUser;


    @FXML
    private Label welcome;

    private static Logger logger=Logger.getLogger(LoginController.class);

    private static final String ADMIN_SCREEN = "admin-screen";
    private static final String MEMBER_SCREEN = "member-screen";

    @FXML
    private void onLoginAction(ActionEvent e) throws Exception {
        onLogin(e);
    }

    public void loadPage(Event e, Member member, String view) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        Parent parent;
        switch (view) {
            case ADMIN_SCREEN:
                loader.setLocation(getClass().getResource("admin-screen.fxml"));
                parent = loader.load();
                SessionManager.member = member;
                MainController adminController = loader.getController();
                System.out.println("test1");
                adminController.setView(MainController.MEMBERS_DASHBOARD);
                System.out.println("test2");
                Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                primaryStage.setScene(new Scene(parent));
                primaryStage.centerOnScreen();
                break;
            case MEMBER_SCREEN:
                loader.setLocation(getClass().getResource("member-screen.fxml"));
                parent = loader.load();
                SessionManager.member = member;
                DashboardController guestController = loader.getController();
                guestController.setView(DashboardController.BOOKS_DASHBOARD);
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(parent));
                break;
            default:
                ErrorPopupComponent.show("Error occurred");

        }

    }

    private Member login(String personalNumber, String password) throws Exception {
        LoginProcessor loginProcessor = new LoginProcessor();
        Member member = loginProcessor.login(personalNumber,password);
        if(member != null) {
            checkSuspensionStatusAndUpdate(member);
        }
            return member;
    }

    private void checkSuspensionStatusAndUpdate(Member member) throws SQLException {
        String query = "SELECT M.isActive, MI.dateOfSuspensions FROM Members M JOIN MembersInformation MI ON M.id = MI.id WHERE M.id = ?";
        DBConnection dbConnection = DBConnection.getConnection();
        try (PreparedStatement selectMemberInfoPstmt = dbConnection.prepareStatement(query)) {

            selectMemberInfoPstmt.setInt(1, member.getId());
            ResultSet rs = selectMemberInfoPstmt.executeQuery();

            if (rs.next()) {
                boolean isActive = rs.getBoolean("isActive");
                Date dateOfSuspension = rs.getDate("dateOfSuspensions");

               if (!isActive && dateOfSuspension != null) {
                    LocalDate suspensionDate = dateOfSuspension.toLocalDate();
                    LocalDate currentDate = LocalDate.now();
                    long daysSinceSuspension = ChronoUnit.DAYS.between(suspensionDate, currentDate);

                   if (daysSinceSuspension > 15) {
                        String updateActiveStatusSql = "UPDATE Members SET isActive = ? WHERE id = ?";
                        try (PreparedStatement updateActiveStatusPstmt = dbConnection.prepareStatement(updateActiveStatusSql)) {
                            updateActiveStatusPstmt.setBoolean(1, true);
                            updateActiveStatusPstmt.setInt(2, member.getId());
                            updateActiveStatusPstmt.executeUpdate();
                            member.setIsActive(true);
                        }
                    } else {
                        throw new SQLException("Your account is suspended.");
                    }
                }
            }
        }
    }

    @FXML
    private void onRegisterAction(MouseEvent e) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
    }

    @FXML
    public void setOnKeyPressed(KeyEvent event) throws Exception {
        if (event.getCode().equals(KeyCode.ENTER)) {
            onLogin(event);
        }
    }

    public void onLogin(Event e)  {



        try {
            String personalNumber = personalNumberField.getText();
            String password = passwordField.getText();

            boolean emptyFields = LoginValidate.validate(personalNumber, password);
            if (emptyFields) {
                ErrorPopupComponent.show("Empty fields");
                return;
            }
            //System.out.println("Jeta");
            Member member = login(personalNumber, password);
            //System.out.println("DONIIIII");

            if (member == null) {
                ErrorPopupComponent.show("Wrong username or password");
                return;
            }
            //System.out.println("ANDIIIII");

            if (member.getRole().equals("5")) {
                //System.out.println("TAVNIKU");
                loadPage(e, member, ADMIN_SCREEN);

            }     else {
                //System.out.println("aRIDONI");
                loadPage(e, member, MEMBER_SCREEN);
            }
        } catch (Exception ex) {
            ErrorPopupComponent.show(ex);
        }

        logger.info("User with ID "+SessionManager.member.getId()+" has been logged in");
    }
}
