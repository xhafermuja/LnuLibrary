package com.example.lnulibrary;

import com.example.lnulibrary.components.ErrorPopupComponent;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.repositories.MemberRepository;
import com.example.lnulibrary.repositories.SuspendedRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuspendedController implements Initializable {
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


    SortedList<Member> sortedData;
    ObservableList<Member> members;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeMembers();
            members = FXCollections.observableArrayList(loadMembers());
            tableView.setItems(members);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    public void initializeMembers() {
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.personalNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personalNumber"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        this.saltColumn.setCellValueFactory(new PropertyValueFactory<>("salt"));
        this.roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        this.isActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
    }

    private ArrayList<Member> loadMembers() throws Exception {
        return SuspendedRepository.getAll();
    }

}
