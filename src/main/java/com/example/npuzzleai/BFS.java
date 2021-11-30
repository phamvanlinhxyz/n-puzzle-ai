package com.example.npuzzleai;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BFS {
    public Node startNode;
    public Node endNode;
    public Node currentNode;
    private final Queue<Node> FRINGE;
    private Vector<Node> CHILD;
    public Vector<Node> RESULT;
    protected int approvedNodes;
    protected int totalNodes;
    protected long time;

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
            System.out.println("Tràn bộ nhớ!");
            System.err.println(e);
        }
    }
    // Truy vết kết quả
    public void addResult(Node n) {
        if(n.parent!=null) {
            addResult(n.parent);
        }
        RESULT.add(n);
    }
}
