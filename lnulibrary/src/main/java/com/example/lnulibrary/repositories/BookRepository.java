package com.example.lnulibrary.repositories;

import com.example.lnulibrary.Book;
import com.example.lnulibrary.Member;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.database.InsertQueryBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class BookRepository {

    private final static DBConnection connection = DBConnection.getConnection();

    private static Book fromResultSet(ResultSet res) throws Exception {
        int ISBN = res.getInt("ISBN");
        String bookTitle = res.getString("title");
        int num_of_copies = res.getInt("num_of_copies");
        String author = res.getString("author");
        int year = res.getInt("year");

        return new Book(ISBN, bookTitle, num_of_copies, author, year);
    }

    public static ArrayList<Book> getAll() throws Exception {
        String query = "SELECT * FROM book";
        ResultSet res = connection.executeQuery(query);

        ArrayList<Book> list = new ArrayList<Book>();
        while (res.next()) {
            list.add(fromResultSet(res));
        }
        return list;
    }

    public static Book findAvailableBook( int num_of_copies, int book_ID, int ISBN, Date return_Date)
            throws Exception {
        String query = "SELECT * FROM book " +
                "WHERE num_of_copies > " +
                "(SELECT COUNT(*) FROM lend_book " +
                "WHERE book_ID = book.ISBN " +
                "AND return_Date IS NULL)";

        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            return fromResultSet(result);
        }

        return null;
    }

    public static Book find(int ISBN) throws Exception {

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book WHERE ISBN = ? LIMIT 1");
        stmt.setInt(1, ISBN);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }
        return fromResultSet(res);
    }
//
//    public static Book find(String bookTitle) throws Exception {
//
//        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE bookTitle = ? LIMIT 1");
//
//        stmt.setString(1, bookTitle);
//        ResultSet res = stmt.executeQuery();
//        if (!res.next()) {
//            return null;
//        }
//        return fromResultSet(res);
//    }

//    public static boolean find(String email, String username) throws Exception {
//        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE email = ? OR username = ?");
//        stmt.setString(1, email);
//        stmt.setString(2, username);
//        ResultSet res = stmt.executeQuery();
//        return res.next();
//    }

    public static Book update(Book model) throws Exception {

        String query = "update book set title = ? , num_of_copies = ?, author = ? , year = ? "
                + "where ISBN = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, model.getTitle());
        stmt.setInt(2, model.getNum_of_copies());
        stmt.setString(3, model.getAuthor());
        stmt.setInt(4, model.getYear());
        stmt.setInt(5, model.getISBN());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows != 1) {
            throw new Exception("ERR_NO_ROW_CHANGE");
        }

        return find(model.getISBN());

    }
//
//    public static Book create(Book model) throws Exception {
//        InsertQueryBuilder query = (InsertQueryBuilder) InsertQueryBuilder.create("books").add("ISBN", 0, "i")
//                .add("bookTitle", model.getBookTitle(), "s")
//                .add("numOfCopies", model.getNumOfCopies(), "i")
//                .add("author", model.getAuthor(), "s")
//                .add("year", model.getYear(), "i");
//
//        int lastInsertedId = connection.execute(query);
//        return find(lastInsertedId);
//    }
//
//    public static boolean remove(int ISBN) throws Exception {
//        String query = "DELETE FROM books WHERE ISBN = ? ";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setInt(1, ISBN);
//        stmt.executeUpdate();
//        Book book = find(ISBN);
//        return book == null;
//    }
}
