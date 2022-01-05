package com.uralsiberianworks.neuralpushkin.api;

public class BotRequestBody {
    String [] history;
    String person;
    int candnum;

    public BotRequestBody(String[] history, String person, int candnum) {
        this.history = history;
        this.person = person;
        this.candnum = candnum;
    }

    public String[] getHistory() {
        return history;
    }

    public void setHistory(String[] history) {
        this.history = history;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getCandnum() {
        return candnum;
    }

    public void setCandnum(int candnum) {
        this.candnum = candnum;
    }
}
