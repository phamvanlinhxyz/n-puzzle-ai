package com.example.npuzzleai;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BFS {
    public Nodes startNode;
    public Nodes endNode;
    public Nodes currentNode;
    private final Queue<Nodes> FRINGE;
    private Vector<Nodes> CHILD;
    public Vector<int[]> RESULT;
    protected int approvedNodes;
    protected int totalNodes;
    protected long time;
    protected boolean stop = false;
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
                if (System.currentTimeMillis() - startTime > 120000) {
                    error = "Thuật toán quá tốn thời gian!";
                    FRINGE.clear();
                    return;
                }
                if (stop) {
                    FRINGE.clear();
                    return;
                }
                if (currentNode.equals(endNode)) {
                    addResult(currentNode);
                    time = System.currentTimeMillis() - startTime;
                    totalNodes = approvedNodes + FRINGE.size();
                    System.out.println("Thuật toán BFS");
                    System.out.println("Số node đã duyệt: " + approvedNodes);
                    System.out.println("Tổng số node trên cây: " + totalNodes);
                    System.out.println("Tổng số bước: " + (RESULT.size() - 1));
                    System.out.println("Thời gian: " + time + " ms");
                    System.out.println("-------------------------");
                    FRINGE.clear();
                    return;
                }
                CHILD = currentNode.successors();
                if (currentNode.parent != null) {
                    for (Nodes child : CHILD) {
                        if (child.equals(currentNode.parent)) {
                            CHILD.removeElement(child);
                            break;
                        }
                    }
                }
                for (Nodes child : CHILD) {
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
    public void addResult(Nodes n) {
        if(n.parent!=null) {
            addResult(n.parent);
        }
        RESULT.add(n.state.value);
    }
}
