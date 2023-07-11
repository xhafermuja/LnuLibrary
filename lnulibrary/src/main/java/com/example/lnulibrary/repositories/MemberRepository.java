package com.example.lnulibrary.repositories;

import com.example.lnulibrary.Member;
import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.database.InsertQueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class MemberRepository {

    private final static DBConnection connection = DBConnection.getConnection();
    public static Member fromResultSet(ResultSet res) throws Exception {
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
        String query = "SELECT * FROM members";
        ResultSet res = connection.executeQuery(query);

        ArrayList<Member> list = new ArrayList<Member>();
        while (res.next()) {
            list.add(fromResultSet(res));
        }
        return list;
    }

    public static ArrayList<Member> getAllInActive() throws Exception {
        String query = "SELECT * FROM members where role = '2'";
        ResultSet res = connection.executeQuery(query);

        ArrayList<Member> list = new ArrayList<Member>();
        while (res.next()) {
            list.add(fromResultSet(res));
        }
        return list;
    }


    public static Member find(int id) throws Exception {

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM members WHERE id = ? LIMIT 1");
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }
        return fromResultSet(res);
    }

    public static Member find(String personalNumber) throws Exception {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM members WHERE personalNumber = ? LIMIT 1");
        stmt.setString(1, personalNumber);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }
        return fromResultSet(res);
    }

    public static boolean find(String personalNumber, String name) throws Exception {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM members WHERE personalNumber = ? OR name = ?");
        stmt.setString(1, personalNumber);
        stmt.setString(2, name);
        ResultSet res = stmt.executeQuery();
        return res.next();
    }

    public static boolean findSuspended(String personalNumber) throws Exception {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM suspend WHERE memberPersonalNumber = ?");
        stmt.setString(1,personalNumber);
        ResultSet res = stmt.executeQuery();
        return res.next();
    }


    public static Member update(Member model) throws Exception {

        String query = "update members set name = ? , password = ? , salt = ? "
                + ", role = ? , isActive = ?  where id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, model.getName());
        stmt.setString(2, model.getPassword());
        stmt.setString(3, model.getSalt());
        stmt.setString(4, model.getRole());
        stmt.setBoolean(5, model.getIsActive());
        stmt.setInt(6, model.getId());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows != 1) {
            throw new Exception("ERR_NO_ROW_CHANGE");
        }

        return find(model.getId());

    }

    public static Member create(Member model) throws Exception {
        InsertQueryBuilder query = (InsertQueryBuilder) InsertQueryBuilder.create("members")
                .add("id", 0, "i")
                .add("personalNumber",model.getPersonalNumber(),"s")
                .add("name", model.getName(), "s")
                .add("password", model.getPassword(), "s")
                .add("salt", model.getSalt(), "s")
                .add("role", model.getRole(), "s")
                .add("isActive", model.getIsActive() ? 1 : 0, "i");

        int lastInsertedId = connection.execute(query);
        return find(lastInsertedId);
    }

    public static boolean remove(int id) throws Exception {
        String query = "DELETE FROM members WHERE id = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        Member member = find(id);
        return member == null;
    }

    public void delete(Member member) throws Exception {
        PreparedStatement stmt;
        stmt = connection.prepareStatement("DELETE FROM members WHERE id = ?");
        stmt.setInt(1, member.getId());
        stmt.executeUpdate();
    }

}