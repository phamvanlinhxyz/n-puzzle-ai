package com.example.npuzzleai;

import java.util.Vector;

public class AStar {
    public Node startNode;
    public Node goalNode;
    public Node currentNode;
    private final Vector<Node> FRINGE;
    private Vector<Node> CHILD;
    public Vector<Node> CLOSED;
    public Vector<int[]> RESULT;
    protected int approvedNodes;
    protected int totalNodes;
    protected long time;
    protected static boolean stop = false;
    protected String error;

    public AStar() {
        FRINGE = new Vector<>();
        CHILD = new Vector<>();
        CLOSED = new Vector<>();
        RESULT = new Vector<>();
    }

    public void solve() {
        RESULT.clear();
        long startTime = System.currentTimeMillis();
        startNode.f = startNode.h = startNode.estimate(goalNode.state);
        startNode.g = 0;
        totalNodes = approvedNodes = 0;
        FRINGE.add(startNode);
        while (!FRINGE.isEmpty()) {
            // Điều kiện dừng thuật toán
            if (System.currentTimeMillis() - startTime > 60000) {
                error = "Thuật toán quá tốn thời gian!";
                approvedNodes = Integer.MAX_VALUE;
                FRINGE.clear();
                CHILD.clear();
                CLOSED.clear();
                return;
            }
            if (stop) {
                FRINGE.clear();
                CHILD.clear();
                CLOSED.clear();
                return;
            }
            // Tìm node có hàm đánh giá - f(n) nhỏ nhất trong FRINGE
            int fMin = FRINGE.get(0).f;
            currentNode = FRINGE.get(0);
            for (Node node : FRINGE) {
                if (node.f < fMin) {
                    fMin = node.f;
                    currentNode = node;
                }
            }
            FRINGE.removeElement(currentNode);
            CLOSED.add(currentNode);
            // Kiểm tra node hiện tại có phải đích hay không
            if (currentNode.h == 0) {
                totalNodes = approvedNodes + FRINGE.size();
                time = System.currentTimeMillis() - startTime;
                addResult(currentNode); // Thêm kết quả vào RESULT
                FRINGE.clear();
                CHILD.clear();
                CLOSED.clear();
                return;
            }
            // Thiết lập các node con
            CHILD = currentNode.successors();
            if (currentNode.parent != null) {
                for (int i = 0; i < CHILD.size(); i++) {
                    Node child = CHILD.get(i);
                    if (child.equals(currentNode.parent)) {
                        CHILD.removeElement(child);
                        break;
                    }
                    // Kiểm tra trạng thái đã được duyệt chưa
                    for (Node node : CLOSED) {
                        if (node.equals(child)) {
                            CHILD.removeElement(child);
                            break;
                        }
                    }
                }
            }
            // Đưa các node con vào FRINGE
            for (Node child : CHILD) {
                child.parent = currentNode;
                child.g = currentNode.g + child.cost;
                child.h = child.estimate(goalNode.state);
                child.f = child.g + child.h;
                // Nếu trạng thái đã tồn tại trong FRINGE và hàm đánh giá tốt hơn => Thay thế
                for (Node node : FRINGE) {
                    if (node.equals(child) && child.f < node.f) {
                        FRINGE.removeElement(node);
                        break;
                    }
                }
                FRINGE.add(0, child);
            }
            CHILD.clear();
            approvedNodes++;
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
