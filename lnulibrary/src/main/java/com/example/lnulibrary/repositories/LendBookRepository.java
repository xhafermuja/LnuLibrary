package com.example.lnulibrary.repositories;

import com.example.lnulibrary.*;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.database.InsertQueryBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Date;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;


public class LendBookRepository {
    private final static DBConnection connection = DBConnection.getConnection();
    private static DoReturn fromResultSet(ResultSet res) throws Exception {
        int lend_ID = res.getInt("lend_ID");
        int book_ID = res.getInt("book_ID");
        int id = res.getInt("id");
        Date lending_Date = res.getDate("lending_Date");
        Date expected_Date = res.getDate("lending_Date");

        return new DoReturn(lend_ID, book_ID, id, lending_Date, expected_Date);
    }
    public static int countLendingsForMember(int member_id) throws SQLException {
        String query = "SELECT COUNT(*) FROM lend_book WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, member_id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count;
    }
    public static DoReturn insertIntoLendBook(Book model) throws Exception {
        Date date1 = new Date(LocalDate.now().getYear()-1900,LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
        Date date2 = new Date(date1.getTime());

        // Add 15 days (in milliseconds) to the time of date2
        date2.setTime(date2.getTime() + 15 * 24 * 60 * 60 * 1000);

       // Date date2 = (Date)
        InsertQueryBuilder query = (InsertQueryBuilder) InsertQueryBuilder.create("lend_book")
                .add("book_id", model.getISBN(), "i")
                .add("id", SessionManager.member.getId(),"i")
                .add("lending_Date",date1 , "d")
                .add("expected_Date",date2, "d");

        int lastInsertedId = connection.execute(query);
        return find(lastInsertedId);
    }

    public static DoReturn find(int id) throws Exception {

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM lend_book WHERE book_ID = ? LIMIT 1");
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }
        return fromResultSet(res);
    }
}
