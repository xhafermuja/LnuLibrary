package com.example.lnulibrary.repositories;

import com.example.lnulibrary.Suspend;
import com.example.lnulibrary.database.DBConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class SuspendRepository {

    private final static DBConnection connection = DBConnection.getConnection();
    private static Suspend fromResultSet(ResultSet res) throws Exception {
        int suspend_ID = res.getInt("suspend_ID");
        int member_ID = res.getInt("member_ID");


        return new Suspend(suspend_ID ,member_ID );
    }
    public static ArrayList<Suspend> getAll() throws Exception {
        String query = "SELECT * FROM suspend";
        ResultSet res = connection.executeQuery(query);

        ArrayList<Suspend> list = new ArrayList<Suspend>();
        while (res.next()) {
            list.add(fromResultSet(res));
        }
        return list;
    }
}