package com.example.npuzzleai;

public class Result {
    public String heuristic;
    public int approved;
    public int total;
    public long time;
    public int step;
    public String error;

    public Result(String heuristic, int approved, int total, int step, long time, String error) {
        this.heuristic = heuristic;
        this.approved = approved;
        this.total = total;
        this.step = step;
        this.time = time;
        this.error = error;
    }

    public String showResult() {
        String rs = "Thuật toán sử dụng Heuristic: " + this.heuristic + "\n";
        if (this.error == null) {
            rs += "Số node đã duyệt: " + this.approved + "\n"
                    + "Tổng số node trên cây: " + this.total + "\n"
                    + "Số bước đi đến đích: " + this.step + "\n"
                    + "Thời gian tìm kiếm: " + this.time + "ms\n";
        } else {
            rs += """
                    Không tìm được lời giải
                    Nguyên nhân:
                    Thuật toán quá tốn thời gian!
                    """;
        }
        return rs;
    }
}
