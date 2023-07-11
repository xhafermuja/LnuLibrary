package com.example.lnulibrary;

import java.util.Date;

public class DoReturn {
    private int lend_ID;
    private int book_ID;
    private int id;
    private Date lending_Date;
    private Date expected_Date;

    public DoReturn(int lend_ID, int book_ID, int id, Date lending_Date, Date expected_Date) {
        this.lend_ID = lend_ID;
        this.book_ID = book_ID;
        this.id = id;
        this.lending_Date = lending_Date;
        this.expected_Date = expected_Date;
    }

    public int getLend_ID() {
        return lend_ID;
    }

    public void setLend_ID(int lend_ID) {
        this.lend_ID = lend_ID;
    }

    public int getBook_ID() {
        return book_ID;
    }

    public void setBook_ID(int book_ID) {
        this.book_ID = book_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLending_Date() {
        return lending_Date;
    }

    public void setLending_Date(Date lending_Date) {
        this.lending_Date = lending_Date;
    }

    public Date getExpected_Date() {
        return expected_Date;
    }

    public void setExpected_Date(Date expected_Date) {
        this.expected_Date = expected_Date;
    }
}
