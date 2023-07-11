package com.example.lnulibrary.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController {

    public static final String LOGOUT_VIEW = "login-view";
    public static final String RESERVATION_ROOM_VIEW = "reservation-rooms";
    public static final String PAYMENT_VIEW = "payments-view";

    public static final String EVENTS_VIEW = "event-view";
    public static final String REGISTER_GUEST = "register-guest-view";

    private static final String OFFERS_VIEW = "offers-view";
    @FXML
    private Label loggedInUser;

    @FXML
    private Button mainBtn;
    @FXML
    private Button reservationsBtn;
    @FXML
    private Button paymentsBtn;
    @FXML
    private Button eventBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Pane mainPane;
    @FXML
    private MenuItem logoutButton;
    @FXML
    private Button offersButton;
    @FXML
    private Button alButton;

    @FXML
    private Button enButton;

    private void changeRunTime(Button button) {
        reservationsBtn.setStyle("-fx-background-color:transparent;");
        paymentsBtn.setStyle("-fx-background-color:transparent;");
        eventBtn.setStyle("-fx-background-color:transparent;");
        logOutBtn.setStyle("-fx-background-color:transparent;");
        offersButton.setStyle("-fx-background-color:transparent;");
        button.setStyle("-fx-background-color:#ab9b81;");
    }

    public void setView(String view) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent parent = null;

        switch (view) {
            case RESERVATION_ROOM_VIEW:
                loader.setLocation(getClass().getResource(setPath(RESERVATION_ROOM_VIEW)));
                changeRunTime(reservationsBtn);
                parent = loader.load();
                break;
            case PAYMENT_VIEW:
                loader.setLocation(getClass().getResource(setPath(PAYMENT_VIEW)));
                changeRunTime(paymentsBtn);
                parent = loader.load();
                break;
            case LOGOUT_VIEW:
                loader.setLocation(getClass().getResource(setPath(LOGOUT_VIEW)));
                changeRunTime(logOutBtn);
                parent = loader.load();
                break;
            case EVENTS_VIEW:
                loader.setLocation(getClass().getResource(setPath(EVENTS_VIEW)));
                changeRunTime(eventBtn);
                parent = loader.load();
                break;
            case OFFERS_VIEW:
                loader.setLocation(getClass().getResource(setPath(OFFERS_VIEW)));
                changeRunTime(offersButton);
                parent = loader.load();
                break;

            default:
        }
        mainPane.getChildren().clear();
        mainPane.getChildren().add(parent);
    }

    @FXML
    private void onOverviewAction(ActionEvent e) throws Exception {
        setView(OFFERS_VIEW);
    }

    @FXML
    private void onReservationAction(ActionEvent e) throws Exception {
        setView(RESERVATION_ROOM_VIEW);
    }

    @FXML
    private void onPaymentsAction(ActionEvent e) throws Exception {
        setView(PAYMENT_VIEW);
    }

    @FXML
    private void onEventsAction(ActionEvent e) throws Exception {
        setView(EVENTS_VIEW);
    }

    @FXML
    private void onLogoutAction(ActionEvent e) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setResizable(false);

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            changeStage(LOGOUT_VIEW, e);
        }

    }

    @FXML
    private void onMenuLogoutAction(ActionEvent e) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource(setPath(LOGOUT_VIEW)));
        Stage stage = (Stage) loggedInUser.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setResizable(false);

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            stage.setScene(new Scene(parent));
        }
    }

    @FXML
    private void onMenuInsertAction(ActionEvent e) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource(setPath(REGISTER_GUEST)));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    @FXML
    private void onMenuAboutAction(ActionEvent e) throws Exception {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/about-view.fxml"))));
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.centerOnScreen();
        stage.show();
    }

    private void changeStage(String path, ActionEvent e) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource(setPath(path)));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private String setPath(String path) {
        return "../views/" + path + ".fxml";
    }

}
