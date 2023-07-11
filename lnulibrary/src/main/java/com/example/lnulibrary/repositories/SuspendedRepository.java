package com.example.lnulibrary.repositories;

import com.example.lnulibrary.Member;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.database.InsertQueryBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SuspendedRepository {

    private final static DBConnection connection = DBConnection.getConnection();
    private static Member fromResultSet(ResultSet res) throws Exception {
        int id = res.getInt("id");
        String personalNumber = res.getString("personalNumber");
        String name = res.getString("name");
        String password = res.getString("password");
        String salt = res.getString("salt");
        String  role = res.getString("role");
        boolean isActive = res.getInt("isActive") == 1;

        return new Member(id, personalNumber, name,  password, salt, role, isActive);
    }

    public static ArrayList<Member> getAll() throws Exception {
        String query = "SELECT * FROM members WHERE isActive = false";
        ResultSet res = connection.executeQuery(query);

        ArrayList<Member> list = new ArrayList<Member>();
        while (res.next()) {
            list.add(fromResultSet(res));
        }
        return list;
    }

}
