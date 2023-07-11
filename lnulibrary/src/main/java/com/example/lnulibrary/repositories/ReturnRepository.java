package com.example.lnulibrary.repositories;

import com.example.lnulibrary.DoReturn;
import com.example.lnulibrary.database.DBConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class ReturnRepository {

    private final static DBConnection connection = DBConnection.getConnection();
    private static DoReturn fromResultSet(ResultSet res) throws Exception {
        int lend_ID = res.getInt("lend_ID");
        int book_ID = res.getInt("book_ID");
        int id = res.getInt("id");
        Date lending_Date = res.getDate("lending_Date");
        Date expected_Date = res.getDate("expected_Date");


        return new DoReturn(lend_ID, book_ID, id, lending_Date,expected_Date);
    }
    public static ArrayList<DoReturn> getAll() throws Exception {
        String query = "SELECT * FROM lend_book";
        ResultSet res = connection.executeQuery(query);

        ArrayList<DoReturn> list = new ArrayList<DoReturn>();
        while (res.next()) {
            list.add(fromResultSet(res));
        }
        return list;
    }
}
