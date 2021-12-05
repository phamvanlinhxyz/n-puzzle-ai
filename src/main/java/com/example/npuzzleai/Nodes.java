package com.example.npuzzleai;

import java.util.Vector;

public class Nodes {
    public State state;
    public int f;
    public int g;
    public int h;
    public int cost;
    public Nodes parent;

    public Nodes(State state, int cost) {
        this.state = state;
        this.cost = cost;
    }
    public boolean equals(Nodes n) {
        boolean flag = true;
        int[] val = state.value;
        int[] newVal = n.state.value;
        for (int i = 0; i < val.length; i++) {
            if (val[i] != newVal[i]) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    public int estimate() {
        return state.chooseHeuristic();
    }
    public Vector<Nodes> successors() {
        Vector<Nodes> nodes = new Vector<>();
        Vector<State> states = state.successors();
        for (State value : states) {
            nodes.add(new Nodes(value, 1));
        }
        return nodes;
    }
}
