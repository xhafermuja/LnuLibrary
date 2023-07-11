package com.example.lnulibrary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController {

    public static final String MEMBERS_DASHBOARD = "members-view";
    public static final String SUSPENDED_DASHBOARD = "suspended-view";
    public static final String BORROWS_DASHBOARD = "borrows-view";

    private static final String VIEW_PATH = "";
    @FXML
    private Label sessionLabel;
    @FXML
    private VBox contentPane;

    @FXML
    private Button membersBtn;
    @FXML
    private Button borrowsBtn;
    @FXML
    private Button suspendedBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label navTitle;
    @FXML
    private Label sectionTitle;
    @FXML
    private Label adminDashboardTitle;


    public void setView(String view) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(this.viewPath(view)));
            Pane pane = null;

            switch (view) {
                case MEMBERS_DASHBOARD:
                    pane = loader.load();
                    contentPane.setAlignment(Pos.TOP_CENTER);
                    break;
                case SUSPENDED_DASHBOARD:
                    pane = loader.load();
                    contentPane.setAlignment(Pos.TOP_CENTER);
                    break;
                case BORROWS_DASHBOARD:
                    pane = loader.load();
                    contentPane.setAlignment(Pos.TOP_CENTER);
                    break;
                default:
                    throw new Exception("ERR_VIEW_NOT_FOUND");
            }
            contentPane.getChildren().clear();
            contentPane.getChildren().add(pane);
            VBox.setVgrow(pane, Priority.ALWAYS);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String viewPath(String view) {
        return view + ".fxml";
    }

    @FXML
    private void onMembersNavClick(ActionEvent ev) {
        try {
            this.setView(MEMBERS_DASHBOARD);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void onSuspendedNavClick(ActionEvent ev) {
        try {
            this.setView(SUSPENDED_DASHBOARD);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void onBorrowsNavClick(ActionEvent ev) {
        try {
            this.setView(BORROWS_DASHBOARD);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    @FXML
    private void onLogoutNavClick(ActionEvent ev) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you sure to log out");
            alert.setResizable(false);

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                stage.setScene(scene);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void onMenuExitAction(ActionEvent e) throws Exception {
        Stage stage = (Stage) sessionLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onMenuLogoutAction(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login-view.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) sessionLabel.getScene().getWindow();
        Scene scene = new Scene(parent);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure to log out");
        alert.setResizable(false);

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            stage.setScene(scene);
        }

    }


    @FXML
    private void onAboutMenuAction(ActionEvent e) throws Exception {
//        Parent parent = FXMLLoader.load(getClass().getResource("../../views/about-view.fxml"));
//        Stage stage = new Stage();
//        stage.setScene(new Scene(parent));
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.show();
    }
}
