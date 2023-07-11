package com.example.lnulibrary;

import com.example.lnulibrary.components.ErrorPopupComponent;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.repositories.MemberRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MembersController implements Initializable {

	private DBConnection connection;
	@FXML
	private TableView<Member> tableView;
	@FXML
	private TableColumn<Member, Integer> idColumn;
	@FXML
	private TableColumn<Member, String> personalNumberColumn;
	@FXML
	private TableColumn<Member, String> nameColumn;
	@FXML
	private TableColumn<Member, String> passwordColumn;
	@FXML
	private TableColumn<Member, String> saltColumn;
	@FXML
	private TableColumn<Member, String> roleColumn;
	@FXML
	private TableColumn<Member, Integer> isActiveColumn;

	@FXML
	private TextField idField;
	@FXML
	private TextField personalNumberField;
	@FXML
	private TextField nameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField saltField;
	@FXML
	private TextField roleField;
	@FXML
	private TextField isActiveField;

	@FXML
	private TextField searchField;
	@FXML
	private Label searchLbl;

	@FXML
	private Button updateBtn;
	@FXML
	private Button deleteBtn;

	@FXML
	private Label idLbl;
	@FXML
	private Label nameLbl;
	@FXML
	private Label usernameLbl;
	@FXML
	private Label emailLbl;
	@FXML
	private Label passwordLbl;
	@FXML
	private Label saltLbl;
	@FXML
	private Label isActiveLbl;
	@FXML
	private Label updatedAtLbl;
	@FXML
	private Label createLbl;
	@FXML
	private Label roleLbl;

	SortedList<Member> sortedData;
	ObservableList<Member> members;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			this.connection = DBConnection.getConnection();
			initializeGuests();
			members = FXCollections.observableArrayList(loadGuests());

			tableView.setItems(members);

			tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tableView.getSelectionModel().selectedItemProperty().addListener((observe, old, _new) -> {
				if (_new != null) {
					try {
						renderGuests(_new);
					} catch (Exception e) {
						ErrorPopupComponent.show(e);
					}
				}
			});

			filterData();
		} catch (Exception ex) {
			ErrorPopupComponent.show(ex);
		}


	}

	private void renderGuests(Member model) throws Exception {
		idField.setText(Integer.toString(model.getId()));
		personalNumberField.setText(model.getPersonalNumber());
		nameField.setText(model.getName());
		passwordField.setText(model.getPassword());
		saltField.setText(model.getSalt());
		roleField.setText(model.getRole());
		isActiveField.setText(Boolean.toString(model.getIsActive()));
	}


	public void clearGuestFields() {
		idField.clear();
		personalNumberField.clear();
		nameField.clear();
		passwordField.clear();
		saltField.clear();
		roleField.clear();
		isActiveField.clear();
		tableView.getSelectionModel().clearSelection();
	}


	@FXML
	private void onUpdateAction(ActionEvent e) throws Exception {

		Member member = new Member(Integer.parseInt(idField.getText()),
				personalNumberField.getText(),
				nameField.getText(),
				passwordField.getText(),
				saltField.getText(),
				roleField.getText(),
				Boolean.parseBoolean(isActiveField.getText()));

		MemberRepository.update(member);

		Member selected = tableView.getSelectionModel().getSelectedItem();
		selected.setPersonalNumber(personalNumberField.getText());
		selected.setName(nameField.getText());
		selected.setPassword(passwordField.getText());
		selected.setSalt(saltField.getText());
		selected.setRole(roleField.getText());
		selected.setIsActive(isActiveField.getText().equals("true") ? true : false);
		tableView.refresh();
		filterData();
		clearGuestFields();
	}

	@FXML
	public void onDeleteAction(ActionEvent e) throws Exception {
		int id = Integer.parseInt(idField.getText());
		if (!MemberRepository.remove(id))
			throw new Exception();

		ArrayList<Member> memberRepo = MemberRepository.getAll();
		members = FXCollections.observableArrayList(memberRepo);
		clearGuestFields();
		filterData();
		tableView.refresh();
	}

	public void filterData() {

		FilteredList<Member> filteredData = new FilteredList<>(members, b -> true);


		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(searchModel -> {
				if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
					return true;
				String searchKeyword = newValue.toLowerCase();
				if (Integer.toString(searchModel.getId()).toLowerCase().indexOf(searchKeyword) != -1) {
					return true;
				} else if (searchModel.getPersonalNumber().toLowerCase().indexOf(searchKeyword) != -1) {
					return true;
				} else if (searchModel.getName().toLowerCase().indexOf(searchKeyword) != -1) {
					return true;
				} else if (searchModel.getSalt().toLowerCase().indexOf(searchKeyword) != -1) {
					return true;
				} else if (searchModel.getPassword().toLowerCase().indexOf(searchKeyword) != -1) {
					return true;
				}
				return false;

			});
		});

		sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(tableView.comparatorProperty());

		tableView.setItems(sortedData);

	}

	public void initializeGuests() {
		this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.personalNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personalNumber"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		this.saltColumn.setCellValueFactory(new PropertyValueFactory<>("salt"));
		this.roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
		this.isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
	}

	ArrayList<Member> loadGuests() throws Exception {
		return MemberRepository.getAll();
	}

}