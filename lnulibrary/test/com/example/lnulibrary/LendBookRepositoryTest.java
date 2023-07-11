package com.example.lnulibrary;

import com.example.lnulibrary.database.DBConnection;
import com.example.lnulibrary.repositories.LendBookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
// Other imports...

class LendBookRepositoryTest {

    private DBConnection connection;
    private LendBookRepository repository;
    private MockedStatic<DBConnection> dbConnectionMock;

    @BeforeEach
    void setUp() {
        connection = Mockito.mock(DBConnection.class);
        dbConnectionMock = Mockito.mockStatic(DBConnection.class);
        dbConnectionMock.when(DBConnection::getConnection).thenReturn(connection);
        repository = new LendBookRepository();
    }

    @AfterEach
    void tearDown() {
        dbConnectionMock.close();
    }

    @Test
    void countLendingsForMember() throws SQLException {
        int memberId = 1;
        int expectedCount = 0;

        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(expectedCount);

        int actualCount = repository.countLendingsForMember(memberId);
        assertEquals(expectedCount, actualCount);

        verify(stmt).setInt(1, memberId);
        verify(stmt).close();
        verify(rs).close();
    }

    @Test
    public void testFindNotFound() throws Exception {
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        DoReturn doReturn = LendBookRepository.find(12345);

        assertNull(doReturn);
    }
}