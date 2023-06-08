package com.slimanice.qlearning.utils;

import jade.core.AID;

import java.util.ArrayList;

public class Path {
    private ArrayList<Action> path;
    private int length;
    private AID agent;

    public Path() {

    }

    public Path(ArrayList<Action> path) {
        this.path = path;
        length = path.size();
    }

    public ArrayList<Action> getPath() {
        return path;
    }

    public void setPath(ArrayList<Action> path) {
        this.path = path;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public AID getAgent() {
        return agent;
    }

    public void setAgent(AID agent) {
        this.agent = agent;
    }
}
