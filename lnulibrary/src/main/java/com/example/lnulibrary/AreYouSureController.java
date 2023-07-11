package com.example.lnulibrary;

import com.example.lnulibrary.repositories.MemberRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AreYouSureController {

    ObservableList<Member> members;
    @FXML
    private void handleYesButtonAction(ActionEvent event) throws Exception {
        int id = SessionManager.member.getId();
        //System.out.println(id);
        if (!MemberRepository.remove(id))
            throw new Exception();

        ArrayList<Member> memberRepo = MemberRepository.getAll();
        members = FXCollections.observableArrayList(memberRepo);

        Parent parent = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent parent;
        loader.setLocation(getClass().getResource("member-screen.fxml"));
        parent = loader.load();
        DashboardController guestController = loader.getController();
        guestController.setView(DashboardController.BOOKS_DASHBOARD);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
    }

}
