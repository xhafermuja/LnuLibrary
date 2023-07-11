package com.example.lnulibrary;

import com.example.lnulibrary.components.ErrorPopupComponent;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.repositories.BookRepository;
import com.example.lnulibrary.repositories.MemberRepository;
import com.example.lnulibrary.repositories.LendBookRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LendBookController implements Initializable {

    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> copiesColumn;
    @FXML
    private TableColumn<Book, Integer> isbnColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, Integer> yearColumn;

    @FXML
    private TextField authorField;

    @FXML
    private Label authorLbl;

    @FXML
    private Button bookBtn;

    @FXML
    private TextField copiesField;

    @FXML
    private Label copiesLbl;

    @FXML
    private TextField isbnField;

    @FXML
    private Label isbnLbl;

    @FXML
    private TextField searchField;

    @FXML
    private Label searchLbl;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TextField titleField;

    @FXML
    private Label titleLbl;

    @FXML
    private TextField yearField;

    @FXML
    private Label yearLbl;

    private Logger logger=Logger.getLogger(LendBookController.class);

    SortedList<Book> sortedData;
    ObservableList<Book> books;
    @FXML
    void onBookAction(ActionEvent event) {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        if (selectedBook != null && selectedBook.getNum_of_copies() > 0) {
            try {
                // check if logged in member can borrow this book based on their role and borrowing limit
                LendBookRepository lendBookRepository = new LendBookRepository();
                BookRepository bookRepository = new BookRepository();
                Member loggedInMember = SessionManager.member;

                int role = Integer.parseInt(loggedInMember.getRole());
                int currentNumberOfBorrows = lendBookRepository.countLendingsForMember(loggedInMember.getId());
                int borrowingLimit = 0;

                // set the borrowing limit based on the member's role
                switch (role) {
                    case 1:
                        borrowingLimit = 3;
                        break;
                    case 2:
                        borrowingLimit = 5;
                        break;
                    case 3:
                        borrowingLimit = 7;
                        break;
                    case 4:
                        borrowingLimit = 10;
                        break;
                }

                //       check if the member has reached their borrowing limit
                if (currentNumberOfBorrows >= borrowingLimit) {
                    // show an alert message that the member has reached their borrowing limit
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Borrowing Limit Reached");
                    alert.setHeaderText("You have reached your borrowing limit");
                    alert.setContentText("Please return some of your borrowed books before borrowing a new one");
                    alert.showAndWait();
                } else {
                    // decrease number of copies in the database
                    selectedBook.setNumOfCopies(selectedBook.getNum_of_copies() - 1);
                    bookRepository.update(selectedBook);

                    lendBookRepository.insertIntoLendBook(selectedBook);

                    // update table view
                    tableView.refresh();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
           //showMessageDialog(null, "There are no available copies of book with ISBN " + selectedBook.getISBN() + ".");
             //   return;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No available copies");
            alert.setHeaderText("There are no more copies left of this book!");
            alert.setContentText("You must wait until someone returns the book!");
            alert.showAndWait();
        }

        logger.info("User with id "+SessionManager.member.getId()+" has borrowed book with ISBN "+selectedBook.getISBN());
    }
private DBConnection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.connection = DBConnection.getConnection();
            initializeBooks();
            books = FXCollections.observableArrayList(loadBooks());

            tableView.setItems(books);

            tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            tableView.getSelectionModel().selectedItemProperty().addListener((observe, old, _new) -> {
                if (_new != null) {
                    try {
                        renderBooks(_new);
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
    private void renderBooks(Book model) throws Exception {
        isbnField.setText(Integer.toString(model.getISBN()));
        titleField.setText(model.getTitle());
        copiesField.setText(Integer.toString(model.getNum_of_copies()));
        authorField.setText(model.getAuthor());
        yearField.setText(Integer.toString(model.getYear()));
    }


    public void initializeBooks() {
        this.isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        this.titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.copiesColumn.setCellValueFactory(new PropertyValueFactory<>("num_of_copies"));
        this.authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        this.yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    public ArrayList<Book> loadBooks() throws Exception {
        BookRepository repository = new BookRepository();
        return repository.getAll();
    }

    public void filterData() {

        FilteredList<Book> filteredData = new FilteredList<>(books, b -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(searchModel -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;
                String searchKeyword = newValue.toLowerCase();
                if (Integer.toString(searchModel.getISBN()).toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                } else if (searchModel.getTitle().toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                } else if (Integer.toString(searchModel.getNum_of_copies()).toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                } else if (searchModel.getAuthor().toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                } else if (Integer.toString(searchModel.getYear()).toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                }
                return false;

            });
        });

        sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }

}
