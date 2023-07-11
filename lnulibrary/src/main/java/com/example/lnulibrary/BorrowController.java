package com.example.lnulibrary;

import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.repositories.BookRepository;
import com.example.lnulibrary.repositories.ReturnRepository;
import com.example.lnulibrary.repositories.MemberRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class BorrowController implements Initializable {
    @FXML
    public AnchorPane booksPane;
    @FXML
    private TableColumn<DoReturn, Integer> lendIdCol;
    @FXML
    private TableColumn<DoReturn, Integer> bookIdCol;
    @FXML
    private TableColumn<DoReturn, Integer> idCol;
    @FXML
    private TableColumn<DoReturn, Date> borrowDateCol;
    @FXML
    private TableColumn<DoReturn, Date> dueDateCol;
    @FXML
    private Button btnReturnBook;
    @FXML
    private AnchorPane eventsPane;
    @FXML
    private TableView<DoReturn> booksTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initializeBooks();
            ObservableList<DoReturn> borrowedBooks = FXCollections.observableArrayList(loadBorrowedBooks());
            booksTableView.setItems(borrowedBooks);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public void initializeBooks() {
        this.lendIdCol.setCellValueFactory(new PropertyValueFactory<>("lend_ID"));
        this.bookIdCol.setCellValueFactory(new PropertyValueFactory<>("book_ID"));
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("lending_Date"));
        this.dueDateCol.setCellValueFactory(new PropertyValueFactory<>("expected_Date"));
    }

    public ArrayList<DoReturn> loadBorrowedBooks() throws Exception {
        ReturnRepository repository = new ReturnRepository();
        return repository.getAll();
    }



        // This method is called when the user clicks the "Return Book" button
        @FXML
        private void returnBookAction(ActionEvent e) throws SQLException {
            DBConnection dbConnection = DBConnection.getConnection();
            DoReturn selectedReturn = booksTableView.getSelectionModel().getSelectedItem();
            int lendId = selectedReturn.getLend_ID();
            int isbn = selectedReturn.getBook_ID();

                    // Retrieve the lending information
                    String selectLendingSql = "SELECT id, expected_date FROM lend_book WHERE lend_id = ?";

            try (PreparedStatement selectLendingPstmt = dbConnection.prepareStatement(selectLendingSql)) {

                selectLendingPstmt.setInt(1, lendId);
                ResultSet rs = selectLendingPstmt.executeQuery();

                if (rs.next()) {
                    int memberId = rs.getInt("id");
                    LocalDate expectedReturnDate = rs.getDate("expected_date").toLocalDate();
                    LocalDate currentDate = LocalDate.now();

                    // Check if the current date has surpassed the expectedReturnDate
                    if (currentDate.isAfter(expectedReturnDate)) {
                        // Increase the numberOfDelays in the MemberInformation table
                        String updateDelaysSql = "UPDATE MembersInformation SET numberOfDelays = numberOfDelays + 1 WHERE id = ?";
                        try (PreparedStatement updateDelaysPstmt = dbConnection.prepareStatement(updateDelaysSql)) {
                            updateDelaysPstmt.setInt(1, memberId);
                            updateDelaysPstmt.executeUpdate();
                        }

                        // Check if the numberOfDelays is greater than 2 and apply suspensions
                        applySuspensions(memberId);
                    }

                    // Increase the number of copies in the Book table and remove the lending record from the Borrows table
                    String updateBookSql = "UPDATE Book SET num_of_copies = num_of_copies + 1 WHERE ISBN = ?";
                    String deleteBorrowSql = "DELETE FROM lend_book WHERE lend_ID = ?";

                    try (PreparedStatement updateBookPstmt = dbConnection.prepareStatement(updateBookSql);
                         PreparedStatement deleteBorrowPstmt = dbConnection.prepareStatement(deleteBorrowSql)) {

                        updateBookPstmt.setInt(1, isbn);
                        updateBookPstmt.executeUpdate();

                        deleteBorrowPstmt.setInt(1, lendId);
                        deleteBorrowPstmt.executeUpdate();
                        booksTableView.getItems().remove(selectedReturn);
                        booksTableView.refresh();
                    }
                }
            } catch (SQLException f) {
                System.out.println("Error in handleReturnBookButtonAction: " + f.getMessage());
            }
        }

    private void applySuspensions(int memberId) {
        DBConnection dbConnection = DBConnection.getConnection();
        String checkDelaysSql = "SELECT numberOfDelays, numberOfSuspensions FROM MembersInformation WHERE id = ?";

        try (PreparedStatement checkDelaysPstmt = dbConnection.prepareStatement(checkDelaysSql)) {
            checkDelaysPstmt.setInt(1, memberId);
            ResultSet rs = checkDelaysPstmt.executeQuery();

            if (rs.next()) {
                int numberOfDelays = rs.getInt("numberOfDelays");
                int numberOfSuspensions = rs.getInt("numberOfSuspensions");

                if (numberOfDelays == 3 || numberOfDelays == 6) {
                    numberOfSuspensions++;

                    if (numberOfSuspensions >= 3) {
                        String selectMemberSuspendedSql = "SELECT personalNumber FROM members WHERE id = ?";
                        String insertMemberToSuspendedSql = "INSERT INTO suspend(memberPersonalNumber) values(?)";
                        String deleteMemberSql = "DELETE FROM Members WHERE id = ?";
                        try (PreparedStatement selectMemberSuspendedPstmt = dbConnection.prepareStatement(selectMemberSuspendedSql);
                                PreparedStatement insertMemberToSuspendedPstmt = dbConnection.prepareStatement(insertMemberToSuspendedSql);
                             PreparedStatement deleteMemberPstmt = dbConnection.prepareStatement(deleteMemberSql);) {
                            selectMemberSuspendedPstmt.setInt(1,memberId);
                            ResultSet resultSet = selectMemberSuspendedPstmt.executeQuery();
                            if(resultSet.next()) {
                                String personalNumber = resultSet.getString("personalNumber");
                                insertMemberToSuspendedPstmt.setString(1, personalNumber);
                                insertMemberToSuspendedPstmt.executeUpdate();
                                deleteMemberPstmt.setInt(1, memberId);
                                deleteMemberPstmt.executeUpdate();
                            }
                        }
                    } else {
                        String updateSuspensionsSql = "UPDATE MembersInformation SET numberOfDelays = 0, numberOfSuspensions = ?, dateOfSuspensions = now() WHERE id = ?";
                        String updateIsActiveQuery = "UPDATE Members SET isActive = ? WHERE id = ?";
                        try (PreparedStatement updateSuspensionsPstmt = dbConnection.prepareStatement(updateSuspensionsSql);
                             PreparedStatement updateActivationPstmt = dbConnection.prepareStatement(updateIsActiveQuery);) {
                            updateSuspensionsPstmt.setInt(1, numberOfSuspensions);
                            updateSuspensionsPstmt.setInt(2, memberId);
                            updateSuspensionsPstmt.executeUpdate();
                            updateActivationPstmt.setBoolean(1,false);
                            updateActivationPstmt.setInt(2,memberId);
                            updateActivationPstmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in applySuspensions: " + e.getMessage());
        }
    }
}
