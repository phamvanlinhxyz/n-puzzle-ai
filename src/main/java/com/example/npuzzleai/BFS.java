package com.example.npuzzleai;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BFS {
    public Node startNode;
    public Node goalNode;
    public Node currentNode;
    private final Queue<Node> FRINGE;
    private Vector<Node> CHILD;
    public Vector<int[]> RESULT;
    protected int approvedNodes;
    protected int totalNodes;
    protected long time;
    protected static boolean stop = false;
    protected String error;

    public BFS() {
        FRINGE = new LinkedList<>();
        CHILD = new Vector<>();
        RESULT = new Vector<>();
    }

    public void solve() {
        RESULT.clear();
        long startTime = System.currentTimeMillis();
        FRINGE.add(startNode);
        try {
            while (!FRINGE.isEmpty()) {
                currentNode = FRINGE.poll();
                if (System.currentTimeMillis() - startTime > 60000) {
                    error = "Thuật toán quá tốn thời gian!";
                    FRINGE.clear();
                    return;
                }
                if (stop) {
                    FRINGE.clear();
                    return;
                }
                if (currentNode.equals(goalNode)) {
                    addResult(currentNode);
                    time = System.currentTimeMillis() - startTime;
                    totalNodes = approvedNodes + FRINGE.size();
                    FRINGE.clear();
                    return;
                }
                CHILD = currentNode.successors();
                if (currentNode.parent != null) {
                    for (Node child : CHILD) {
                        if (child.equals(currentNode.parent)) {
                            CHILD.removeElement(child);
                            break;
                        }
                    }
                }
                for (Node child : CHILD) {
                    child.parent = currentNode;
                    FRINGE.add(child);
                }
                CHILD.clear();
                approvedNodes++;
            }
        } catch (OutOfMemoryError e) {
            FRINGE.clear();
            error = "Tràn bộ nhớ!";
            e.printStackTrace();
        }
    }
    // Truy vết kết quả
    public void addResult(Node n) {
        if(n.parent!=null) {
            addResult(n.parent);
        }
        RESULT.add(n.state.value);
    }
}
