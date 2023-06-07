package com.slimanice.qlearning.utils;

public class Action {
    int currI, currJ;
    String action;

    public Action(int currI, int currJ, String action) {
        this.currI = currI;
        this.currJ = currJ;
        this.action = action;
    }

    public int getCurrI() {
        return currI;
    }

    public void setCurrI(int currI) {
        this.currI = currI;
    }

    public int getCurrJ() {
        return currJ;
    }

    public void setCurrJ(int currJ) {
        this.currJ = currJ;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
