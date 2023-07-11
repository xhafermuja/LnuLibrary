package com.example.lnulibrary;
import com.example.lnulibrary.repositories.MemberRepository;
import com.example.lnulibrary.database.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MemberRepositoryTest {

    private DBConnection connection;
    private MemberRepository repository;
    private MockedStatic<DBConnection> dbConnectionMock;

    @BeforeEach
    void setUp() {
        connection = Mockito.mock(DBConnection.class);
        dbConnectionMock = Mockito.mockStatic(DBConnection.class);
        dbConnectionMock.when(DBConnection::getConnection).thenReturn(connection);
        repository = new MemberRepository();
    }

    @AfterEach
    void tearDown() {
        dbConnectionMock.close();
    }



    @Test
    public void testFromResultSet() throws Exception {

        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("personalNumber")).thenReturn("12345");
        when(rs.getString("name")).thenReturn("John Doe");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("salt")).thenReturn("salt");
        when(rs.getString("role")).thenReturn("user");
        when(rs.getInt("isActive")).thenReturn(1);

        Member member = repository.fromResultSet(rs);
        assertNotNull(member);
    }


    @Test
    public void testFindById() throws Exception {
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("personalNumber")).thenReturn("12345");
        when(rs.getString("name")).thenReturn("John Doe");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("salt")).thenReturn("salt");
        when(rs.getString("role")).thenReturn("user");
        when(rs.getInt("isActive")).thenReturn(1);

        Member member = repository.find(1);

        assertNotNull(member);
        assertEquals(1, member.getId());
        assertEquals("John Doe", member.getName());
    }


    @Test
    public void testCreate() throws Exception {
        // Prepare the mock objects
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        // Mock the execution of the insert statement
        when(connection.prepareStatement(anyString())).thenReturn(stmt); // Use the existing prepareStatement method
        when(stmt.executeUpdate()).thenReturn(1);
        when(stmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        // Mock the execution of the find statement
        PreparedStatement findStmt = Mockito.mock(PreparedStatement.class);
        ResultSet findRs = Mockito.mock(ResultSet.class);
        when(connection.prepareStatement(anyString())).thenReturn(findStmt);
        when(findStmt.executeQuery()).thenReturn(findRs);
        when(findRs.next()).thenReturn(true);

        when(findRs.getInt("id")).thenReturn(1);
        when(findRs.getString("personalNumber")).thenReturn("12345");
        when(findRs.getString("name")).thenReturn("John Doe");
        when(findRs.getString("password")).thenReturn("password");
        when(findRs.getString("salt")).thenReturn("salt");
        when(findRs.getString("role")).thenReturn("user");
        when(findRs.getInt("isActive")).thenReturn(1);

        // Test the create method
        Member newMember = new Member(1,"12345", "John Doe", "password", "salt", "user", true);
        Member createdMember = MemberRepository.create(newMember);

        assertNotNull(createdMember);
        assertEquals(1, createdMember.getId());
        assertEquals("John Doe", createdMember.getName());
    }

}

