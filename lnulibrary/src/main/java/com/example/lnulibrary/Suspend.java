package com.example.lnulibrary;

public class Suspend {
    private int suspend_ID;
    private int member_ID;

    public Suspend(int suspend_ID, int member_ID) {
        this.suspend_ID = suspend_ID;
        this.member_ID = member_ID;
    }

    public int getSuspend_ID() {
        return suspend_ID;
    }

    public void setSuspend_ID(int suspend_ID) {
        this.suspend_ID = suspend_ID;
    }

    public int getMember_ID() {
        return member_ID;
    }

    public void setMember_ID(int member_ID) {
        this.member_ID = member_ID;
    }
}
