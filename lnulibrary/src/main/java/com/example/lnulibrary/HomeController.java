package com.example.lnulibrary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {


	public void changeScreen(ActionEvent event) throws Exception {
		onLogin(event);
	}

	@FXML
	private void onLogin(ActionEvent e) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("login-view.fxml"));
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		Scene scene = new Scene(parent);
		stage.setScene(scene);


	}
}