package com.example.lnulibrary;

import java.util.Date;

public class Return {

private int return_ID;
private int member_ID;
private int book_ID;
private Date return_Date;

    public Return(){};
    public Return(int return_ID, int member_ID, int book_ID, Date return_Date) {
        this.return_ID = return_ID;
        this.member_ID = member_ID;
        this.book_ID = book_ID;
        this.return_Date = return_Date;
    }

    public int getReturn_ID() {
        return return_ID;
    }

    public void setReturn_ID(int return_ID) {
        this.return_ID = return_ID;
    }

    public int getMember_ID() {
        return member_ID;
    }

    public void setMember_ID(int member_ID) {
        this.member_ID = member_ID;
    }

    public int getBook_ID() {
        return book_ID;
    }

    public void setBook_ID(int book_ID) {
        this.book_ID = book_ID;
    }

    public Date getReturn_Date() {
        return return_Date;
    }

    public void setReturn_Date(Date return_Date) {
        this.return_Date = return_Date;
    }
}
