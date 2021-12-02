package com.example.npuzzleai;

import java.util.*;

public class State {
    public static int heuristic = 0;
    public int[] value;
    //public boolean check = false;
    private final int size;
    private final int length;
    private int blank;
    private int count = 0;
    public State(int m) { //truyền vào kích thước của puzzle
        this.size = m;
        this.length = size * size;
        this.value= new int[length];
        this.blank = 0;
    }
    public State(int[] v, int size) {//truyền vào trạng thái và kích thước của puzzle
        this.value = v;
        this.size = size;
        this.length = size * size;
        this.blank = posBlank(this.value);
    }
    public void Init() {
        for (int i = 0; i < length; i++) {
            value[i] = i;
        }
    }
    public int[] createStartArray() {
        Init();
        return value;
    }
    public int[] createRandomArray() {
        Init();
        Random rand = new Random();
        /*if (size < 3) {
            int iRand;
            for (int i = 0; i < length; i++) {
                while ((iRand = rand.nextInt(length)) == i) ;
                if (rand.nextInt(length) != i) {
                    int tmp = value[i];
                    value[i] = value[iRand];
                    value[iRand] = tmp;
                }
            }
        } else {*/
            int t = 86;
            count = 0;
            int a = 1, b = 0;
            do {
                switch (a) {
                    case 1 -> DOWN();
                    case 2 -> RIGHT();
                    case 3 -> LEFT();
                    case 4 -> UP();
                }
                while (true) {
                    b = rand.nextInt(4) + 1;
                    if ((a == 1 && b != 4) || (a == 4 && b != 1) || (a == 2 && b != 3) || (a == 3 && b != 2)) {
                        a = b;
                        break;
                    }
                }
            } while (count != t);
        //}
        return value;
    }
    public int chooseHeuristic() {
        int est = 0;
        if (heuristic == 1) est = heuristic1();
        else if (heuristic == 2) est = heuristic2();
        else if (heuristic == 3) est = heuristic3();
        else if (heuristic == 4) est = heuristic4();
        else if (heuristic == 5) est = heuristic5();
        return est;
    }
    public int posBlank(int[] val) {
        int pos = 0;
        for (int i = 0; i < val.length; i++) {
            if (val[i] == 0) {
                pos = i;
                break;
            }
        }
        return pos;
    }
    public boolean isGoal() {
        boolean flag = true;
        for (int i = 0; i < length; i++) {
            if (value[i] != i) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    // Tổng số ô sai vị trí
    public int heuristic1() {
        int distance = 0;
        for (int i = 0; i < length; i++) {
            if(value[i] != 0 && value[i] != i) distance++;
        }
        return distance;
    }
    // Tổng khoảng cách để đưa các ô về đúng vị trí
    public int heuristic2() {
        int distance = 0;
        for (int i = 0; i < length; i++) {
            int c = value[i];
            if (c != 0) {
                distance += Math.abs(c / size - i / size) + Math.abs(c % size - i % size);
            }
        }
        return distance;
    }
    // Tổng khoảng cách Euclid của các ô với vị trí đích
    public int heuristic3() {
        int distance = 0;
        for (int i = 0; i < length; i++) {
            int c = value[i];
            if (c != 0) {
                int width = Math.abs(c % size - i % size);
                int height = Math.abs(c / size - i / size);
                distance += (int) Math.sqrt(width * width + height * height);
            }
        }
        return distance;
    }
    // Tổng số ô sai hàng và số ô sai cột
    public int heuristic4() {
        int distance = 0;
        for (int i = 0; i < length; i++) {
            int c = value[i];
            if (c != 0) {
                if ((c / size) != (i / size)) distance++;
                if ((c % size) != (i % size)) distance++;
            }
        }
        return distance;
    }
    // Tổng khoảng cách để đưa các ô về đúng vị trí + số ô xung đột tuyến tính
    public int heuristic5() {
        int distance = 0;
        distance += heuristic2();
        for (int i = 0; i < size; i++) {
            int max = -1;
            for (int j = 0; j < size; j++) {
                int c = value[i * size + j];
                if(c != 0 && c / size == i) {
                    if (c > max) max = c;
                    else distance += 2;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            int max = -1;
            for (int j = 0; j < size; j++) {
                int c = value[i + j * size];
                if (c != 0 && c % size == i) {
                    if (c > max) max = c;
                    else distance += 2;
                }
            }
        }
        return distance;
    }
    public Vector<State> successors() {
        Vector<State> states = new Vector<>();
        int blank = posBlank(value);
        if (blank / size > 0) {
            addSuccessor(blank, blank - size, states, value);
        }
        if (blank / size < size - 1) {
            addSuccessor(blank, blank + size, states, value);
        }
        if (blank % size > 0) {
            addSuccessor(blank, blank - 1, states, value);
        }
        if (blank % size < size - 1) {
            addSuccessor(blank, blank + 1, states, value);
        }
        return states;
    }
    public void addSuccessor(int oldBlank, int newBlank, Vector<State> states, int[] oldVal) {
        int[] newVal = oldVal.clone();
        newVal[oldBlank] = newVal[newBlank];
        newVal[newBlank] = 0;
        states.add(new State(newVal, size));
    }
    public void DOWN() {
        blank = posBlank(value);
        int temp;
        if(blank < length - size) {
            temp = value[blank];
            value[blank] = value[blank + size];
            value[blank + size] = temp;
            blank += size;
            count++;
        }
        else return;
    }

    public void UP() {
        blank = posBlank(value);
        int temp;
        if(blank >= size) {
            temp = value[blank];
            value[blank] = value[blank - size];
            value[blank - size] = temp;
            blank -= size;
            count++;
        }
        else return;
    }

    public void RIGHT() {
        blank = posBlank(value);
        int temp;
        if(blank % size != size-1) {
            temp = value[blank];
            value[blank] = value[blank + 1];
            value[blank + 1] = temp;
            blank += 1;
            count++;
        }
        else return;
    }

    public void LEFT() {
        blank = posBlank(value);
        int temp;
        if(blank % size != 0 ) {
            temp = value[blank];
            value[blank] = value[blank - 1];
            value[blank - 1] = temp;
            blank -= 1;
            count++;
        }
        else return;
    }
}
